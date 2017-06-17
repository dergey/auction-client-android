package com.zhuravlev.sergey.auction.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.acl.Owner;
import java.sql.Timestamp;

public class Lot implements Parcelable {

    private Long id;
    private String title;
    private String description;
    private String image;
    private Timestamp expirationDate;
    private Double startingPrice;
    private Double auctionStep;
    private Integer status;
    private User owner;
    private Category category;
    @JsonIgnore
    private Bet lastBet;

    public Lot(Long id, String title, String description, String image, Double startingPrice, Double auctionStep, Timestamp millis) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.startingPrice = startingPrice;
        this.auctionStep = auctionStep;
        this.expirationDate = millis;
    }

    public Lot(String title, String description, String image, Double startingPrice, Double auctionStep, Timestamp millis) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.startingPrice = startingPrice;
        this.auctionStep = auctionStep;
        this.expirationDate = millis;
    }

    public Lot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Double getAuctionStep() {
        return auctionStep;
    }

    public void setAuctionStep(Double auctionStep) {
        this.auctionStep = auctionStep;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @JsonIgnore
    public Bet getLastBet() {
        return lastBet;
    }

    @JsonProperty
    public void setLastBet(Bet lastBet) {
        this.lastBet = lastBet;
    }

    @JsonIgnore
    public Double getLastPrice() {
        if (lastBet != null) return getLastBet().getSize();
        return getStartingPrice();
    }

    @JsonIgnore
    public String getLastPriceInString() {
        if (lastBet == null) return getStartingPrice().toString() + " руб.";
        return getLastBet().getSize() + " руб., сделана " + getLastBet().getBuyer().getUsername();
    }

    //------------------------------------------------------------ Передача в Intent

    protected Lot(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        image = in.readString();
        expirationDate = new Timestamp(in.readLong());
        startingPrice = in.readDouble();
        auctionStep = in.readDouble();
        status = in.readInt();
        owner = in.readParcelable(User.class.getClassLoader());
        category = in.readParcelable(Category.class.getClassLoader());
        lastBet = in.readParcelable(Bet.class.getClassLoader());
    }

    public static final Creator<Lot> CREATOR = new Creator<Lot>() {
        @Override
        public Lot createFromParcel(Parcel in) {
            return new Lot(in);
        }

        @Override
        public Lot[] newArray(int size) {
            return new Lot[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeLong(expirationDate.getTime());
        parcel.writeDouble(startingPrice);
        parcel.writeDouble(auctionStep);
        parcel.writeInt(status);
        parcel.writeParcelable(owner, i);
        parcel.writeParcelable(category, i);
        parcel.writeParcelable(lastBet, i);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Lot{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", expirationDate=" + expirationDate +
                ", startingPrice=" + startingPrice +
                ", auctionStep=" + auctionStep +
                ", status=" + status +
                ", owner=" + owner +
                ", category=" + category +
                ", lastBet=" + lastBet +
                '}';
    }
}