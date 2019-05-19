package com.sergey.zhuravlev.auction.client.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sergey.zhuravlev.auction.client.enums.LotStatus;

import java.io.Serializable;
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
    private Long startingAmount;

    @JsonProperty(value = "currency")
    private String currencyCode;

    @JsonProperty(value = "auction_step")
    private Long auctionStep;

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
        startingAmount = in.readLong();
        currencyCode = in.readString();
        auctionStep = in.readLong();
        status = LotStatus.valueOf(in.readString());
        owner = in.readString();
        category = in.readString();
    }

    public static final Creator<ResponseLotDto> CREATOR = new Creator<ResponseLotDto>() {
        @Override
        public ResponseLotDto createFromParcel(Parcel in) {
            return new ResponseLotDto(in);
        }

        @Override
        public ResponseLotDto[] newArray(int size) {
            return new ResponseLotDto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        String[] imageArray = images.toArray(new String[0]);
        parcel.writeInt(imageArray.length);
        parcel.writeStringArray(imageArray);
        parcel.writeLong(createAt.getTime());
        parcel.writeLong(updateAt.getTime());
        parcel.writeLong(expiresAt.getTime());
        parcel.writeLong(startingAmount);
        parcel.writeString(currencyCode);
        parcel.writeLong(auctionStep);
        parcel.writeString(status.name());
        parcel.writeString(owner);
        parcel.writeString(category);
    }

}
