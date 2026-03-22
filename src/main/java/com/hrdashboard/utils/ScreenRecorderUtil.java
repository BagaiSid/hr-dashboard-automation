package com.hrdashboard.utils;

import org.monte.media.Format;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class ScreenRecorderUtil {

    private static final ThreadLocal<ScreenRecorder> recorder = new ThreadLocal<>();
    private static final String RECORDING_DIR = "target/recordings";

    private ScreenRecorderUtil() {}

    public static void startRecording(String testName) {
        if (shouldSkipRecording()) return;

        try {
            File recordDir = new File(RECORDING_DIR);
            if (!recordDir.exists()) recordDir.mkdirs();

            GraphicsConfiguration gc = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

            ScreenRecorder sr = new ScreenRecorder(gc, gc.getBounds(),
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO,
                            EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24, FrameRateKey, new Rational(15, 1),
                            QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO,
                            EncodingKey, "black",
                            FrameRateKey, new Rational(30, 1)),
                    null, recordDir);

            sr.start();
            recorder.set(sr);
        } catch (Exception e) {
            System.out.println("[ScreenRecorder] Recording not available: " + e.getMessage());
        }
    }

    public static File stopRecording() {
        ScreenRecorder sr = recorder.get();
        if (sr == null) return null;

        try {
            sr.stop();
            java.util.List<File> files = sr.getCreatedMovieFiles();
            recorder.remove();
            return files.isEmpty() ? null : files.get(files.size() - 1);
        } catch (Exception e) {
            System.out.println("[ScreenRecorder] Error stopping recording: " + e.getMessage());
            recorder.remove();
            return null;
        }
    }

    private static boolean shouldSkipRecording() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"))
                || GraphicsEnvironment.isHeadless();
    }
}
