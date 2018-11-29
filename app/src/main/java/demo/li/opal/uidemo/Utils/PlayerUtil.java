package demo.li.opal.uidemo.Utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by rincliu on 2016/2/24.
 */
public class PlayerUtil {

    private static AudioManager sManager;

    /**
     *
     */
    public static class Player extends MediaPlayer {
        private boolean isPlaying;

        @Override
        public boolean isPlaying() {
            return isPlaying;
        }

        @Override
        public void start() {
            if (isPlaying) return;
            try {
                super.start();
            } catch (Exception e) {
                return;
            }
            isPlaying = true;
        }

        @Override
        public void pause() {
            if (!isPlaying) return;
            try {
                super.pause();
            } catch (Exception e) {
                return;
            }
            isPlaying = false;
        }

        @Override
        public void stop() {
            try {
                super.stop();
            } catch (Exception e) {
                return;
            }
            isPlaying = false;
        }

        @Override
        public void reset() {
            try {
                super.reset();
            } catch (Exception e) {
                return;
            }
            isPlaying = false;
        }

        @Override
        public void release() {
            try {
                super.release();
            } catch (Exception e) {
                return;
            }
            isPlaying = false;
        }
    }

    public static Player createPlayer(Context context, String audioPath, boolean isLoop) {
        if (audioPath.startsWith(FileUtils.RES_PREFIX_ASSETS)) {
            return createPlayerFromAssets(context, FileUtils.getRealPath(audioPath), isLoop);
        } else {
            return createPlayerFromUri(context, audioPath, isLoop);
        }
    }

    /**
     * @param context
     * @param uriStr
     * @param isLoop
     * @return
     */
    public static Player createPlayerFromUri(Context context, String uriStr, boolean isLoop) {
        Player mPlayer = null;
        try {
            mPlayer = new Player();
            mPlayer.setDataSource(context, Uri.parse(uriStr));
            preparePlayer(mPlayer, isLoop);
            return mPlayer;
        } catch (IOException e) {
            destroyPlayer(mPlayer);
            return null;
        }
    }

    /**
     * @param context
     * @param assetsPath
     * @param isLoop
     * @return
     */
    public static Player createPlayerFromAssets(Context context, String assetsPath, boolean isLoop) {
        Player mPlayer = null;
        try {
            mPlayer = new Player();
            AssetFileDescriptor afd = context.getAssets().openFd(assetsPath);
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            preparePlayer(mPlayer, isLoop);
            return mPlayer;
        } catch (IOException e) {
            destroyPlayer(mPlayer);
            return null;
        }
    }

    private static void preparePlayer(Player player, boolean isLoop) throws IOException {
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setLooping(isLoop);
        //audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        player.setOnCompletionListener(new Player.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                ((Player) mediaPlayer).isPlaying = false;
            }
        });
        player.setOnErrorListener(new Player.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                destroyPlayer((Player) mediaPlayer);
                return true;
            }
        });
        // 这里call SubtitleController.selectDefaultTrack()的时候可能有mHandler未初始化完毕导致的空指针问题
        player.prepare();
    }

    /**
     * @param player
     * @param reset
     */
    public static void startPlayer(Player player, boolean reset) {
        if (player != null) {
            if (reset) {
                player.setOnSeekCompleteListener(new Player.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mediaPlayer) {
                        ((Player) mediaPlayer).start();
                    }
                });
                player.seekTo(0);
            } else {
                player.start();
            }
        }
    }

    /**
     * @param player
     */
    public static void seekPlayer(Player player, int time) {
        if (player != null) {
            player.setOnSeekCompleteListener(new Player.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer) {
                    ((Player) mediaPlayer).start();
                }
            });
            player.seekTo(time);

        }
    }

    /**
     * @param player
     */
    public static void stopPlayer(Player player) {
        if (player != null) {
            player.pause();
        }
    }

    /**
     * @param player
     */
    public static void destroyPlayer(Player player) {
        if (player != null) {
            player.stop();
            player.reset();
            player.release();
        }
    }

    /**
     * @param context
     * @param mute
     */
    public static void setMute(Context context, boolean mute) {
        getAudioManager(context).setStreamMute(AudioManager.STREAM_MUSIC, mute);
    }

//    private static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
//        @Override
//        public void onAudioFocusChange(int focusChange) {
//            switch (focusChange) {
//                case AudioManager.AUDIOFOCUS_GAIN:
//                    // resume playback;
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS:
//                    // Lost focus for an unbounded amount of time;
//                    // stop playback and release media player;
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                    // Lost focus for a short time, but we have to stop playback.
//                    // We don't release the media player because playback is likely to resume
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                    // Lost focus for a short time, but it's ok to keep playing at an attenuated level
//                    break;
//            }
//        }
//    };

    /**
     *
     */
    public static class Recorder extends MediaRecorder {
        private boolean isRecording;

        @Override
        public void start() {
            if (isRecording) return;
            try {
                super.start();
            } catch (Exception e) {
                return;
            }
            isRecording = true;
        }

        @Override
        public void stop() {
            if (!isRecording) return;
            try {
                super.stop();
            } catch (Exception e) {
                return;
            }
            isRecording = false;
        }

        @Override
        public void reset() {
            if (!isRecording) return;
            try {
                super.reset();
            } catch (Exception e) {
                return;
            }
            isRecording = false;
        }

        @Override
        public void release() {
            try {
                super.release();
            } catch (Exception e) {
                return;
            }
            isRecording = false;
        }

        /**
         * @return
         */
        public boolean isRecording() {
            return isRecording;
        }
    }

//    private static final String RECORD_AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO;
//
//    /**
//     * @param context
//     */
//    public static boolean checkRecordPermission(Context context) {
//        return ContextCompat.checkSelfPermission(context, RECORD_AUDIO_PERMISSION) != PackageManager.PERMISSION_GRANTED;
//    }
//
//    /**
//     *
//     * @param activity
//     * @param requestCode
//     */
//    public static void requestRecordPermission(Activity activity, int requestCode) {
//        ActivityCompat.requestPermissions(activity, new String[]{RECORD_AUDIO_PERMISSION}, requestCode);
//    }
//
//    private final int REQUEST_CODE_REQUEST_AUDIO_RECORD_PERMISSION = 0x99999;
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_REQUEST_AUDIO_RECORD_PERMISSION: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted
//                } else {
//                    // permission denied
//                }
//                break;
//            }
//        }
//    }

    /**
     * @param outputFile
     * @return
     */
    public static Recorder createRecorder(String outputFile) {
        Recorder mRecorder = null;
        try {
            mRecorder = new Recorder();
            mRecorder.setOutputFile(outputFile);
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setAudioChannels(1);
            mRecorder.setAudioSamplingRate(44100);
            mRecorder.setAudioEncodingBitRate(96000);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mediaRecorder, int what, int extra) {
                    destroyRecorder((Recorder) mediaRecorder);
                }
            });
            mRecorder.prepare();
        } catch (Exception e) {
            destroyRecorder(mRecorder);
            mRecorder = null;
        }
        return mRecorder;
    }

    private boolean isSupportedAudioSamplingRate(int audioSamplingRate) {
        return AudioRecord.getMinBufferSize(audioSamplingRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) > 0;
    }

    /**
     * @param mRecorder
     * @return
     */
    public static void startRecorder(Recorder mRecorder) {
        if (mRecorder != null) {
            mRecorder.start();
        }
    }

    /**
     * @param mRecorder
     */
    public static void destroyRecorder(Recorder mRecorder) {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * @param context
     * @param volume
     */
    public static void setVolume(Context context, int volume) {
        AudioManager audioManager = getAudioManager(context);
        volume = Math.max(volume, 0);
        volume = Math.min(volume, getMaxVolume(context));
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    /**
     * @param context
     */
    public static int getVolume(Context context) {
        return getAudioManager(context).getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * @param context
     */
    public static int getMaxVolume(Context context) {
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * @param context
     * @return
     */
    public static boolean isSilentMode(Context context) {
        switch (getAudioManager(context).getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                return false;
            case AudioManager.RINGER_MODE_SILENT:
                return true;
            case AudioManager.RINGER_MODE_VIBRATE:
                return true;
            default:
                return false;
        }
    }

    private static AudioManager getAudioManager(Context context) {
        if (sManager == null) {
            sManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        return sManager;
    }


    //////////////////////////////////////////////////////////
//    private static final int RECORDER_SAMPLE_RATE = 8000;
//    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
//    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int BUFFER_ELEMENTS = 1024;
//    private static final int BYTES_PER_BUFFER_ELEMENT = 2;
//    private AudioRecord recorder = null;
//    private Thread recordingThread = null;
//    private boolean isRecording = false;
//
//    private void startRecording(final String outputFilePath) {
//        if (!isRecording) {
//            //int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
//            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
//                    RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING,
//                    BUFFER_ELEMENTS * BYTES_PER_BUFFER_ELEMENT);
//            recorder.startRecording();
//            if (recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
//                recordingThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        writeAudioDataToFile(outputFilePath);
//                    }
//                }, "AudioRecord Thread");
//                recordingThread.start();
//                isRecording = true;
//            }
//        }
//    }
//
//    private void stopRecording() {
//        if (isRecording) {
//            if (recorder != null) {
//                recorder.stop();
//                recorder.release();
//                recorder = null;
//            }
//            recordingThread = null;
//            isRecording = false;
//        }
//    }
//
//    private void writeAudioDataToFile(String filePath) {
//        short data[] = new short[BUFFER_ELEMENTS];
//        FileOutputStream os = null;
//        try {
//            os = new FileOutputStream(filePath);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        while (isRecording) {
//            recorder.read(data, 0, BUFFER_ELEMENTS);
//            try {
//                os.write(short2byte(data), 0, BUFFER_ELEMENTS * BYTES_PER_BUFFER_ELEMENT);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            os.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private byte[] short2byte(short[] data) {
//        int length = data.length;
//        byte[] bytes = new byte[length * 2];
//        for (int i = 0; i < length; i++) {
//            bytes[i * 2] = (byte) (data[i] & 0x00FF);
//            bytes[(i * 2) + 1] = (byte) (data[i] >> 8);
//            data[i] = 0;
//        }
//        return bytes;
//    }

}
