package ru.noties.performance_test;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */
public class Configuration implements Parcelable {

    private final BuildConfig.ORM[] orms;
    private final OpType[] opTypes;
    private final int[] rounds;
    private final Time time;

    public Configuration(BuildConfig.ORM[] orms, OpType[] opTypes, int[] rounds, Time time) {
        this.orms = orms;
        this.opTypes = opTypes;
        this.rounds = rounds;
        this.time = time;
    }

    public BuildConfig.ORM[] getOrms() {
        return orms;
    }

    public OpType[] getOpTypes() {
        return opTypes;
    }

    public int[] getRounds() {
        return rounds;
    }

    public Time getTime() {
        return time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeIntArray(getOrdinals(orms));
        dest.writeIntArray(getOrdinals(opTypes));

        dest.writeIntArray(this.rounds);
        dest.writeInt(this.time == null ? -1 : this.time.ordinal());
    }

    private int[] getOrdinals(Enum<?>[] e) {
        final int[] o = new int[e.length];
        for (int i = 0; i < o.length; i++) {
            o[i] = e[i].ordinal();
        }
        return o;
    }

    private Configuration(Parcel in) {
        final int[] ormsO       = in.createIntArray();
        final int[] opTypesO    = in.createIntArray();

        final BuildConfig.ORM[] orms = new BuildConfig.ORM[ormsO.length];
        fromOrdinals(ormsO, BuildConfig.ORM.values(), orms);
        this.orms = orms;

        final OpType[] opTypes = new OpType[opTypesO.length];
        fromOrdinals(opTypesO, OpType.values(), opTypes);
        this.opTypes = opTypes;

        this.rounds = in.createIntArray();
        int tmpTime = in.readInt();
        this.time = tmpTime == -1 ? null : Time.values()[tmpTime];
    }

    private <E extends Enum<?>> void fromOrdinals(int[] o, E[] all, E[] toFill) {
        for (int i = 0; i < o.length; i++) {
            toFill[i] = all[o[i]];
        }
    }

    public static final Parcelable.Creator<Configuration> CREATOR = new Parcelable.Creator<Configuration>() {
        public Configuration createFromParcel(Parcel source) {
            return new Configuration(source);
        }

        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
    };
}
