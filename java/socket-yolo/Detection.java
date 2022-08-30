package detection;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Detection {
    private String modelWeights;
    private String modelConfiguration;
    private Net net;
    private List<String> outBlobNames;
    private Size size;

     // yolo 실행 시 필요한 파일, 객체를 준비하는 생성자
    public Detection(){
        // opencv의 네이티브 라이브러리 로드
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // YOLO 에 필요한 파일 로드
        modelWeights = "yolo/yolov4-tiny-custom.weights";
        modelConfiguration = "yolo/yolov4-tiny-custom714.cfg";

        // YOLO 다크넷 모델 생성
        net = Dnn.readNetFromDarknet(modelConfiguration, modelWeights);

        // 학습된 객체의 목록을 해시형태로 받아옴
        outBlobNames = getOutPutNames(net);

        size = new Size(288, 288);
    }


    // 웹캠에서 frame 을 받아와 객체 탐지 작업을 한 뒤에 사각형이 쳐진 frame 을 반환한다.
    public BufferedImage yolo(BufferedImage image) throws Exception{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        Mat frame = Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);

        List<Mat> result = new ArrayList<>();

        // 한 프레임을 네트워크에 입력함
        Mat blob = Dnn.blobFromImage(frame, 0.00392, size, new Scalar(0), true, false);

        net.setInput(blob);

        net.forward(result, outBlobNames);

        float confThreshold = 0.6f;
        List<Integer> clsIds = new ArrayList<>();
        List<Float> confs = new ArrayList<>();
        List<Rect2d> rects = new ArrayList<>();

        for (int i = 0; i < result.size(); ++i) {
            Mat level = result.get(i);

            for (int j = 0; j < level.rows(); ++j) {
                Mat row = level.row(j);
                Mat scores = row.colRange(5, level.cols());
                Core.MinMaxLocResult mm = Core.minMaxLoc(scores);
                float confidence = (float) mm.maxVal;
                Point classIdPoint = mm.maxLoc;

                if (confidence > confThreshold) {
                    //scaling for drawing the bounding boxes//
                    int centerX = (int) (row.get(0, 0)[0] * frame.cols());
                    int centerY = (int) (row.get(0, 1)[0] * frame.rows());
                    int width = (int) (row.get(0, 2)[0] * frame.cols());
                    int height = (int) (row.get(0, 3)[0] * frame.rows());
                    int left = centerX - width / 2;
                    int top = centerY - height / 2;

                    clsIds.add((int) classIdPoint.x);
                    confs.add(confidence);
                    rects.add(new Rect2d(left, top, width, height));
                }else {  //추가된 코드
                    clsIds.add((int) 0);
                    confs.add((float) 0);
                    rects.add(new Rect2d(0, 0, 0, 0));
                }
            }
        }

        float nmsThresh = 0.5f;
        MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confs));
        Rect2d[] boxesArray = rects.toArray(new Rect2d[0]);
        MatOfRect2d boxes = new MatOfRect2d(boxesArray);
        MatOfInt indices = new MatOfInt();

        Dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThresh, indices);

        if (indices.empty()) {
            System.out.println("error detect");
            return image;
        }
        else {
            int[] ind = indices.toArray();
            for (int i = 0; i < ind.length; ++i) {
                int idx = ind[i];
                Rect2d box = boxesArray[idx];
                Imgproc.rectangle(frame, box.tl(), box.br(), new Scalar(0, 0, 255), 2);
            }
        }
        return Mat2BufferedImage(frame);
    }

    // 학습된 객체 리스트
    private static List<String> getOutPutNames(Net net) {
        List<String> names = new ArrayList<>();

        List<Integer> outLayers = net.getUnconnectedOutLayers().toList();
        List<String> layersNames = net.getLayerNames();

        outLayers.forEach((item) -> names.add(layersNames.get(item - 1)));
        return names;
    }

    private static BufferedImage Mat2BufferedImage(Mat matrix) throws Exception {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, matOfByte);
        byte ba[] = matOfByte.toArray();
        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(ba));
        return bi;
    }
}
