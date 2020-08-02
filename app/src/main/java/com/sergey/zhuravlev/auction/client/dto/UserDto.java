package com.sergey.zhuravlev.auction.client.dto;

import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Parcelable {

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "phone")
    private String phone;

    @JsonProperty(value = "account")
    private AccountResponseDto account;

    public boolean isIncomplete() {
        return account == null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeParcelable(this.account, flags);
    }

    protected UserDto(android.os.Parcel in) {
        this.email = in.readString();
        this.phone = in.readString();
        this.account = in.readParcelable(AccountResponseDto.class.getClassLoader());
    }

    public static final Creator<UserDto> CREATOR = new Creator<UserDto>() {
        @Override
        public UserDto createFromParcel(android.os.Parcel source) {
            return new UserDto(source);
        }

        @Override
        public UserDto[] newArray(int size) {
            return new UserDto[size];
        }
    };

}
