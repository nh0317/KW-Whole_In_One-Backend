package com.naturemobility.seoul.domain.userPayment;

public enum CardCode {
    BC("BC","361"),
    Gwangju("공주","364"),
    SamSung("삼성","365"),
    Shinhan("신한","366"),
    Hyundai("현대","367"),
    Lotte("롯데","368"),
    Suhyup("수협","369"),
    City("씨티","370"),
    NH("NH","371"),
    Jeonbuk("전북","372"),
    Jeju("제주","373"),
    Hana("하나SK","374"),
    KB("KB국민","381"),
    Woori("우리","041"),
    PostOffice("우체국","071"),
    Unknown("알 수 없음","0");

    private final String cardCo;
    private String cardCode;
    CardCode(String cardCo, String cardCode) {
        this.cardCo=cardCo;
        this.cardCode=cardCode;
    }
    public static String cardCoOf(String cardCode){
        switch (cardCode){
            case "361":
                return BC.cardCo;
            case "364":
                return Gwangju.cardCo;
            case "365":
                return SamSung.cardCo;
            case "366":
                return Shinhan.cardCo;
            case "367":
                return Hyundai.cardCo;
            case "368":
                return Lotte.cardCo;
            case "369":
                return Suhyup.cardCo;
            case "370":
                return City.cardCo;
            case "371":
                return NH.cardCo;
            case "372":
                return Jeonbuk.cardCo;
            case "373":
                return Jeju.cardCo;
            case "374":
                return Hana.cardCo;
            case "381":
                return KB.cardCo;
            case "041":
                return Woori.cardCo;
            case "071":
                return PostOffice.cardCo;
            default:
                return Unknown.cardCo;
        }
    }
}
