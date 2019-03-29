package com.sergey.zhuravlev.auction.client.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sergey.zhuravlev.auction.client.enums.LotStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLotDto implements Parcelable, Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "images")
    private Collection<String> images;

    @JsonProperty(value = "create_at")
    private Date createAt;

    @JsonProperty(value = "update_at")
    private Date updateAt;

    @JsonProperty(value = "expires_at")
    private Date expiresAt;

    @JsonProperty(value = "starting_amount")
    private BigDecimal startingAmount;

    @JsonProperty(value = "currency_code")
    private String currencyCode;

    @JsonProperty(value = "auction_step")
    private BigDecimal auctionStep;

    @JsonProperty(value = "status")
    private LotStatus status;

    @JsonProperty(value = "owner")
    private String owner;

    @JsonProperty(value = "category")
    private String category;

    //

    public ResponseLotDto(Parcel in) {
        title = in.readString();
        description = in.readString();
        int imagesCount = in.readInt();
        String[] imagesArray = new String[imagesCount];
        in.readStringArray(imagesArray);
        images = Arrays.asList(imagesArray);
        createAt = new Date(in.readLong());
        updateAt = new Date(in.readLong());
        expiresAt = new Date(in.readLong());
        startingAmount = BigDecimal.valueOf(in.readDouble());
        currencyCode = in.readString();
        auctionStep = BigDecimal.valueOf(in.readDouble());
        status = LotStatus.valueOf(in.readString());
        owner = in.readString();
        category = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        String[] imageArray = (String[]) images.toArray();
        parcel.writeInt(imageArray.length);
        parcel.writeStringArray(imageArray);
        parcel.writeLong(createAt.getTime());
        parcel.writeLong(updateAt.getTime());
        parcel.writeLong(expiresAt.getTime());
        parcel.writeDouble(startingAmount.doubleValue());
        parcel.writeString(currencyCode);
        parcel.writeDouble(auctionStep.doubleValue());
        parcel.writeString(status.name());
        parcel.writeString(owner);
        parcel.writeString(category);
    }

}
