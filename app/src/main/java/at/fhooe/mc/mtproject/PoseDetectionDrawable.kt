package at.fhooe.mc.mtproject

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import at.fhooe.mc.mtproject.helpers.GraphicOverlay
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class PoseDetectionDrawable(
    overlay: GraphicOverlay,
    val pose: Pose,
    private val thresholdIFL: Double,
) : GraphicOverlay.Graphic(overlay) {
    private var mPaint: Paint = Paint()
    private var mFacePaint: Paint = Paint()
    private var mArmPaint: Paint = Paint()
    private var mChestPaint: Paint = Paint()
    private var mLegPaint: Paint = Paint()
    private var mHandPaint: Paint = Paint()
    private var mFootPaint: Paint = Paint()
    private var mTextPaint: Paint = Paint()
    private var mClassificationPaint: Paint = Paint()
    private var mPointPaint: Paint = Paint()
    private var mRightPaint: Paint = Paint()
    private var mLeftPaint: Paint = Paint()
    private lateinit var mCanvas: Canvas

    init {
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = STROKE_WIDTH
        mPaint.style = Paint.Style.STROKE

        mPointPaint.color = Color.WHITE
        mPointPaint.strokeWidth = STROKE_WIDTH
        mPointPaint.style = Paint.Style.FILL

        mFacePaint.color = Color.WHITE
        mFacePaint.strokeWidth = STROKE_WIDTH
        mFacePaint.style = Paint.Style.FILL

        mArmPaint.color = Color.WHITE
        mArmPaint.strokeWidth = STROKE_WIDTH
        mArmPaint.style = Paint.Style.FILL

        mChestPaint.color = Color.WHITE
        mChestPaint.strokeWidth = STROKE_WIDTH
        mChestPaint.style = Paint.Style.FILL

        mLegPaint.color = Color.WHITE
        mLegPaint.strokeWidth = STROKE_WIDTH
        mLegPaint.style = Paint.Style.FILL

        mHandPaint.color = Color.WHITE
        mHandPaint.strokeWidth = STROKE_WIDTH
        mHandPaint.style = Paint.Style.FILL

        mFootPaint.color = Color.WHITE
        mFootPaint.strokeWidth = STROKE_WIDTH
        mFootPaint.style = Paint.Style.FILL

        mFootPaint.color = Color.WHITE
        mFootPaint.strokeWidth = STROKE_WIDTH
        mFootPaint.style = Paint.Style.FILL

        mRightPaint.color = Color.RED
        mRightPaint.strokeWidth = STROKE_WIDTH
        mRightPaint.style = Paint.Style.FILL

        mLeftPaint.color = Color.parseColor("#007EB8")
        mLeftPaint.strokeWidth = STROKE_WIDTH
        mLeftPaint.style = Paint.Style.FILL

        mTextPaint.color = Color.WHITE
        mTextPaint.textSize = DEBUG_TEXT_WIDTH
        mTextPaint.setShadowLayer(10.0f, 0f, 0f, Color.BLACK)
        mTextPaint.style = Paint.Style.FILL

        mClassificationPaint.color = Color.WHITE
        mClassificationPaint.textSize = POSE_CLASSIFICATION_TEXT_SIZE
        mClassificationPaint.setShadowLayer(10.0f, 0f, 0f, Color.BLACK)
        mClassificationPaint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas?) {
        if (canvas != null) {
            mCanvas = canvas
        }

        val landmarks = pose.allPoseLandmarks
        if (landmarks.isEmpty()) {
            return
        }

        initLandmarks(mCanvas)

        for (landmark in landmarks) {
            if (checkPoint(landmark)) {
                drawPoint(mCanvas, landmark, mPointPaint)
            }
        }
    }

    private fun initLandmarks(canvas: Canvas) {
        val nose = pose.getPoseLandmark(PoseLandmark.NOSE)
        val leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)
        val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
        val leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER)
        val rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER)
        val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)
        val rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER)
        val leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)
        val rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)

        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

        // Face
        drawLine(canvas, nose, leftEyeInner, mFacePaint)
        drawLine(canvas, leftEyeInner, leftEye, mFacePaint)
        drawLine(canvas, leftEye, leftEyeOuter, mFacePaint)
        drawLine(canvas, nose, rightEyeInner, mFacePaint)
        drawLine(canvas, rightEyeInner, rightEye, mFacePaint)
        drawLine(canvas, rightEye, rightEyeOuter, mFacePaint)
        drawLine(canvas, leftMouth, rightMouth, mFacePaint)

        // Chest
        drawLine(canvas, leftShoulder, rightShoulder, mChestPaint)
        drawLine(canvas, leftShoulder, leftHip, mChestPaint)
        drawLine(canvas, rightShoulder, rightHip, mChestPaint)

        // Arms
        drawLine(canvas, leftShoulder, leftElbow, mLeftPaint)
        drawLine(canvas, leftElbow, leftWrist, mLeftPaint)
        drawLine(canvas, rightShoulder, rightElbow, mRightPaint)
        drawLine(canvas, rightElbow, rightWrist, mRightPaint)

        // Legs
        drawLine(canvas, leftHip, rightHip, mLegPaint)
        drawLine(canvas, leftHip, leftKnee, mLeftPaint)
        drawLine(canvas, leftKnee, leftAnkle, mLeftPaint)
        drawLine(canvas, rightHip, rightKnee, mRightPaint)
        drawLine(canvas, rightKnee, rightAnkle, mRightPaint)
    }

    private fun drawPoint(canvas: Canvas, landmark: PoseLandmark, paint: Paint) {
        if (landmark.inFrameLikelihood >= thresholdIFL) {
            val point = landmark.position3D
            canvas.drawCircle(translateX(point.x), translateY(point.y), DOT_RADIUS, paint)
        }
    }

    private fun drawLine(
        canvas: Canvas,
        startLandmark: PoseLandmark?,
        endLandmark: PoseLandmark?,
        paint: Paint
    ) {
        if (startLandmark != null && endLandmark != null && startLandmark.inFrameLikelihood >= thresholdIFL && endLandmark.inFrameLikelihood >= thresholdIFL) {
            val start = startLandmark.position3D
            val end = endLandmark.position3D

            canvas.drawLine(
                translateX(start.x),
                translateY(start.y),
                translateX(end.x),
                translateY(end.y),
                paint
            )
        }
    }

    private fun checkPoint(point: PoseLandmark): Boolean {
        when (point.landmarkType) {
            PoseLandmark.LEFT_EAR -> return false
            PoseLandmark.RIGHT_EAR -> return false
            PoseLandmark.LEFT_PINKY -> return false
            PoseLandmark.RIGHT_PINKY -> return false
            PoseLandmark.LEFT_INDEX -> return false
            PoseLandmark.RIGHT_INDEX -> return false
            PoseLandmark.LEFT_THUMB -> return false
            PoseLandmark.RIGHT_THUMB -> return false
            PoseLandmark.LEFT_HEEL -> return false
            PoseLandmark.RIGHT_HEEL -> return false
            PoseLandmark.LEFT_FOOT_INDEX -> return false
            PoseLandmark.RIGHT_FOOT_INDEX -> return false
        }
        return true
    }

    private companion object {
        const val DOT_RADIUS = 7.0f
        const val STROKE_WIDTH = 6.0f
        const val DEBUG_TEXT_WIDTH = 45.0f
        const val POSE_CLASSIFICATION_TEXT_SIZE = 60.0f
    }
}