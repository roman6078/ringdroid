package com.ringdroid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * Created by milton on 17/3/6.
 */

public class FadeInAndOutEffect extends AudioEffect {
    public FadeInAndOutEffect(EffectManager effectManager) {
        super(effectManager);
    }

    @Override
    public ByteBuffer handle(ByteBuffer byteBuffer, float playStartMsec, float playEndMsec) {
        ByteBuffer mDecodedBytes = ByteBuffer.allocate(byteBuffer.capacity());
        mDecodedBytes.order(ByteOrder.LITTLE_ENDIAN);
        int mPlaySampleStart = (int) (playStartMsec * (mEffectManager.mSoundFile.getSampleRate())) * mEffectManager.mSoundFile.getChannels();
        int mPlaySampleEnd = (int) (playEndMsec * (mEffectManager.mSoundFile.getSampleRate())) * mEffectManager.mSoundFile.getChannels();
        int size = mPlaySampleEnd - mPlaySampleStart;
        double rate;
        int limitLen = (int) (size * 0.2);
        ShortBuffer mDecodedSamples = byteBuffer.asShortBuffer();
        for (int i = mPlaySampleStart; i < size; i++) {
            rate = 1f;
            if ((i - mPlaySampleStart) <= limitLen) {
                //淡入
                rate = (double) (i - mPlaySampleStart) / limitLen;
            }

            if ((size - i - 1) <= limitLen) {
                //淡出
                rate = (double) (size - i - 1) / limitLen;
            }

            if (rate < 0.0f) {
                rate = 0.0f;
            }
            if (rate <= 1.0f && rate >= 0.0f) {
                mDecodedBytes.putShort(i * 2, (short) (mDecodedSamples.get(i) * rate));
            }
        }

        return mDecodedBytes;
    }

}
