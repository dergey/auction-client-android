package com.sergey.zhuravlev.auction.client.dto;

import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto implements Parcelable {

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "photo")
    private String photo;

    @JsonProperty(value = "firstname")
    private String firstname;

    @JsonProperty(value = "lastname")
    private String lastname;

    @JsonProperty(value = "stars")
    private BigDecimal stars;

    @JsonProperty(value = "bio")
    private String bio;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.photo);
        dest.writeString(this.firstname);
        dest.writeString(this.lastname);
        dest.writeSerializable(this.stars);
        dest.writeString(this.bio);
    }

    protected AccountResponseDto(android.os.Parcel in) {
        this.username = in.readString();
        this.photo = in.readString();
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.stars = (BigDecimal) in.readSerializable();
        this.bio = in.readString();
    }

    public static final Creator<AccountResponseDto> CREATOR = new Creator<AccountResponseDto>() {
        @Override
        public AccountResponseDto createFromParcel(android.os.Parcel source) {
            return new AccountResponseDto(source);
        }

        @Override
        public AccountResponseDto[] newArray(int size) {
            return new AccountResponseDto[size];
        }
    };

}
