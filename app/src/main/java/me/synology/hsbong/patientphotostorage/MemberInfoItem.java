package me.synology.hsbong.patientphotostorage;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by bongh on 2018-10-13.
 */

public class MemberInfoItem {
    public String name;
    public String phone;
    public String email;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
