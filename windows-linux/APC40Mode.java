enum APC40Mode {
    Generic, AbletonLive, AlternateAbletonLive;

    public byte getByte() {
        switch (this) {
            case Generic:
                return (byte) 0x40;
            case AbletonLive:
                return (byte) 0x41;
            case AlternateAbletonLive:
                return (byte) 0x42;
        }
        throw new RuntimeException("Unsupported enum: " + this);
    }

    @Override
    public String toString() {
        switch (this) {
            case Generic:
                return "Generic Mode";
            case AbletonLive:
                return "Ableton Live Mode";
            case AlternateAbletonLive:
                return "Alternate Ableton Live Mode";
        }
        throw new RuntimeException("Unsupported enum: " + this);
    }
}
