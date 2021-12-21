package com.nhom9.orderfoodserver.Common;

import com.nhom9.orderfoodserver.Model.User;


public class Common {
    public static User currentUser;
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final int PICK_IMAGE_REQUEST =71;
    public static String convertCodeToStatus(String code){
        //khi admin thay đổi trạng thái đơn hàng ở package orderstatus thì common này có nhiệm vụ return lại qua bên app user
        if (code.equals("0"))
            return "Đã đặt hàng";
        else if (code.equals("1"))
            return "Đang gửi thức ăn";
        else
            return "Đã gửi thức ăn";

    }
}
//ok nha, xong app admin, còn cái holder thì lên mạng tìm hiểu thì sẽ rõ hơn anh nói đấy
