package com.zhuravlev.sergey.auction.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.sql.Timestamp;


public class Bet implements Parcelable {

    private Long id;
    private Timestamp time;
    private Double size;
    private Lot lot;
    private User buyer;

    public Bet(Double size, Long lotId) {
        this.size = size;
        Lot lot = new Lot();
        lot.setId(lotId);
        this.lot = lot;
        Log.d("Auction.Bet", this.lot.toString());
    }

    public Bet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    protected Bet(Parcel in) {
        id = in.readLong();
        time = new Timestamp(in.readLong());
        size = in.readDouble();
        lot = in.readParcelable(Lot.class.getClassLoader());
        buyer = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Bet> CREATOR = new Creator<Bet>() {
        @Override
        public Bet createFromParcel(Parcel in) {
            return new Bet(in);
        }

        @Override
        public Bet[] newArray(int size) {
            return new Bet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(time.getTime());
        parcel.writeDouble(size);
        parcel.writeParcelable(lot, i);
        parcel.writeParcelable(buyer, i);
    }
}
