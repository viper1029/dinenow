import {Platform} from 'react-native';

module.exports = {
    mainGreen: "#00c497",

    toolbarDefaultBg: "#00c497",
    statusBarColor: "#00c497",

    fontSizeBase: 15,
    titleFontSize: 18,

    btnTextSizeSmall: this.fontSizeBase * .8,
    btnTextSize: this.fontSizeBase * 1.1,
    btnTextSizeLarge: this.fontSizeBase * 1.5,

    iconFontSize: 30,
    rowIconFontSize: 30,

    toolbarHeight: (Platform.OS === 'ios' ) ? 70 : 55,

    brandPrimary: "#428bca",
    brandInfo: "#5bc0de",
    brandSuccess: "#5cb85c",
    brandDanger: "#d9534f",
    brandWarning: "#f0ad4e",
    brandSidebar: "#252A30",

    brandSecondary: "#00c497",   //new style

    inverseTextColor: "#000",
    textColor: "#fff",

    rowUnderLayColor: "#48BBEC",

    generalRowStyle: {

    },

    get fontSizeH1() {
        return this.fontSizeBase * 1.8;
    },
    get fontSizeH2() {
        return this.fontSizeBase * 1.6;
    },
    get fontSizeH3() {
        return this.fontSizeBase * 1.4;
    },

    get iconSizeLarge() {
        return this.iconFontSize * 1.4;
    },
    get iconSizeSmall() {
        return this.iconFontSize * .6;
    },

    borderRadiusBase: 4,

    get borderRadiusLarge() {
        return this.fontSizeBase * 3.8;
    },

    footerHeight: 55,


    toolbarInverseBg: "#222",

    get btnPrimaryBg() {
        return this.brandPrimary;
    },
    get btnPrimaryColor() {
        return this.inverseTextColor;
    },
    get btnSuccessBg() {
        return this.brandSuccess;
    },
    get btnSuccessColor() {
        return this.inverseTextColor;
    },
    get btnDangerBg() {
        return this.brandDanger;
    },
    get btnDangerColor() {
        return this.inverseTextColor;
    },
    get btnInfoBg() {
        return this.brandInfo;
    },
    get btnInfoColor() {
        return this.inverseTextColor;
    },
    get btnWarningBg() {
        return this.brandWarning;
    },
    get btnWarningColor() {
        return this.inverseTextColor;
    },

    borderWidth: 1,

    get inputColor() {
        return this.textColor;
    },
    get inputColorPlaceholder() {
        return 'rgba(255, 255, 255, 1.0)';
    },

    inputBorderColor: "#fff",
    inputHeightBase: 40,
    inputGroupMarginBottom: 10,
    inputPaddingLeft: 5,
    get inputPaddingLeftIcon() {
        return this.inputPaddingLeft * 8;
    },

    dropdownBg: "#000",
    dropdownLinkColor: "#414142",

    jumbotronPadding: 30,
    jumbotronBg: "#C9C9CE",

    contentPadding: 10,

    listBorderColor: "rgba(181, 181, 181, 0.34)",
    listDividerBg: "#f2f2f2",
    listItemPadding: 15,
    listNoteColor: "#ddd",
    listBg: "#fff",



    badgeColor: "#fff",
    badgeBg: "#ED1727",

    lineHeight: 21,

    defaultSpinnerColor: "#45D56E",
    inverseSpinnerColor: "#1A191B",

    defaultProgressColor: "#E4202D",
    inverseProgressColor: "#1A191B"
}
