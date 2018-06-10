package com.example.android.datafrominternet.HaveFun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.example.android.datafrominternet.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

/**
 * Created by ucla on 2018/6/7.
 */

public class Emojify {

    private static final float EMOJI_SCALE_FACTOR = .9f;

    public static Bitmap detectFaces(Context context, Bitmap picture){
        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        if (!faceDetector.isOperational()) {
            Log.d("test","wrong");
            Toast.makeText(context, "未监测到Google Play，请安装后重试", Toast.LENGTH_SHORT).show();
        }

        Frame frame = new Frame.Builder().setBitmap(picture).build();

        SparseArray<Face> faces = faceDetector.detect(frame);

        Log.d("test", "检测到的人脸数目 = " + faces.size());

        if(faces.size() == 0){
            Toast.makeText(context, "里面没脸", Toast.LENGTH_SHORT).show();
        }

        Bitmap resultBitmap = picture;

        for (int i=0; i<faces.size();i++){
            Face face = faces.valueAt(i);
            Bitmap emojiBitmap;
            switch (getClassifications(face)) {
                case SMILE:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.smiling_face_with_smiling_eyes);
                    break;
                case SAD:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.worried_face);
                    break;
                case LEFT_WINK:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.winking_face_left);
                    break;
                case RIGHT_WINK:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.winking_face);
                    break;
                case CLOSED_EYE_SMILE:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.white_smiling_face);
                    break;
                case CLOSED_EYE_SAD:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.disappointed_face);
                    break;
                case SLIGHTLY_SMILE:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.slightly_smile);
                case CLOSED_LARGE_SMILE:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.closed_smile_large);
                case LARGE_SMILE:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.smile_open_mouth);
                default:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_launcher);
            }

            resultBitmap = addBitmapToFace(resultBitmap, emojiBitmap, face);

        }

        faceDetector.release();
        return resultBitmap;
    }

    private static Emoji getClassifications(Face face){
        Log.d("test", "getClassifications: smilingProb = "
                + face.getIsSmilingProbability());
        Log.d("test", "getClassifications: leftEyeOpenProb = "
                + face.getIsLeftEyeOpenProbability());
        Log.d("test", "getClassifications: rightEyeOpenProb = "
                + face.getIsRightEyeOpenProbability());
        Emoji emoji = null;
        double isSmile = 0.3;
        double isSad = 0.1;
        double eyeIsOpen = 0.7;
        double isHappy = 0.7;
        boolean smile = face.getIsSmilingProbability() > isSmile;
        boolean leftEyeOpen = face.getIsLeftEyeOpenProbability() > eyeIsOpen;
        boolean rightEyeOpen = face.getIsRightEyeOpenProbability() > eyeIsOpen;
        boolean sad = face.getIsSmilingProbability() < isSad;
        boolean veryHappy = face.getIsSmilingProbability() > isHappy;
        boolean notHappy =  face.getIsSmilingProbability() < isSmile && face.getIsSmilingProbability()>isSad;

        if(smile) {
            if (!leftEyeOpen && rightEyeOpen) {
                emoji = Emoji.RIGHT_WINK;
            }  else if(!rightEyeOpen && leftEyeOpen){
                emoji = Emoji.LEFT_WINK;
            } else if (!rightEyeOpen){
                emoji = Emoji.CLOSED_EYE_SMILE;
            } else {
                emoji = Emoji.SMILE;
            }
        } else if(sad){
            if (!leftEyeOpen && !rightEyeOpen) {
                emoji = Emoji.CLOSED_EYE_SAD;
            } else {
                emoji = Emoji.SAD;
            }
        } else if(notHappy){
            emoji = Emoji.SLIGHTLY_SMILE;
        }else if(veryHappy){
            if (!leftEyeOpen && !rightEyeOpen) {
                emoji = Emoji.CLOSED_LARGE_SMILE;
            }else emoji = Emoji.LARGE_SMILE;
        } else {
            emoji = Emoji.NEUTRAL;
        }

        return emoji;


    }

    private static Bitmap addBitmapToFace(Bitmap backgroundBitmap, Bitmap emojiBitmap, Face face) {

        Bitmap resultBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),
                backgroundBitmap.getHeight(), backgroundBitmap.getConfig());

        float scaleFactor = EMOJI_SCALE_FACTOR;

        int newEmojiWidth = (int) (face.getWidth() * scaleFactor);
        int newEmojiHeight = (int) (emojiBitmap.getHeight() *
                newEmojiWidth / emojiBitmap.getWidth() * scaleFactor);

        emojiBitmap = Bitmap.createScaledBitmap(emojiBitmap, newEmojiWidth, newEmojiHeight, false);

        float emojiPositionX =
                (face.getPosition().x + face.getWidth() / 2) - emojiBitmap.getWidth() / 2;
        float emojiPositionY =
                (face.getPosition().y + face.getHeight() / 2) - emojiBitmap.getHeight() / 3;

        // Create the canvas and draw the bitmaps to it
        Canvas canvas = new Canvas(resultBitmap);

        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        canvas.drawBitmap(emojiBitmap, emojiPositionX, emojiPositionY, null);

        return resultBitmap;
    }

    private enum Emoji {
        SMILE,
        SAD,
        LEFT_WINK,
        RIGHT_WINK,
        CLOSED_EYE_SMILE,
        CLOSED_EYE_SAD,
        NEUTRAL,
        CLOSED_LARGE_SMILE,
        LARGE_SMILE,
        SLIGHTLY_SMILE
    }


}
