package com.hwaipy.rrdps;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Hwaipy
 */
public class Decoder {

    private final Collection<Tagger.Entry> tags;
    private final TimeEventList aliceQRNG;
    private final TimeEventList bobQRNG;

    public Decoder(Collection<Tagger.Entry> tags, TimeEventList aliceQRNG, TimeEventList bobQRNG) {
        this.tags = tags;
        this.aliceQRNG = aliceQRNG;
        this.bobQRNG = bobQRNG;
    }

    public ArrayList<Entry> decode() {
        ArrayList<Entry> result = new ArrayList<>();
        for (Tagger.Entry tag : tags) {
            int roundIndex = tag.getRoundIndex();
            int pulseIndex = tag.getPulseIndex();
            int decode = tag.getCode();
            EncodingRandom encodingRandom = ((ExtandedTimeEvent<EncodingRandom>) aliceQRNG.get(roundIndex)).getProperty();
            DecodingRandom decodingRandom = ((ExtandedTimeEvent<DecodingRandom>) bobQRNG.get(roundIndex)).getProperty();
            int delay = decodingRandom.getDelay();
            int encode = encodingRandom.getEncode(pulseIndex, delay);
            if (encode >= 0) {
                Entry entry = new Entry(roundIndex, pulseIndex, encode, decode);
                result.add(entry);
            }
        }
        return result;
    }

    public class Entry {

        private final int roundIndex;
        private final int pulseIndex;
        private final int encode;
        private final int decode;

        private Entry(int roundIndex, int pulseIndex, int encode, int decode) {
            this.roundIndex = roundIndex;
            this.pulseIndex = pulseIndex;
            this.encode = encode;
            this.decode = decode;
        }

        public int getRoundIndex() {
            return roundIndex;
        }

        public int getPulseIndex() {
            return pulseIndex;
        }

        public int getEncode() {
            return encode;
        }

        public int getDecode() {
            return decode;
        }
    }
}
