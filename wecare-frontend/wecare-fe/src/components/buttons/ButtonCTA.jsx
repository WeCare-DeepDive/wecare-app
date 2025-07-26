import * as React from "react";
import {Image, StyleSheet, Text, View} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import Iconbell from "../assets/iconbell.svg"
import Iconplus from "../assets/iconplus.svg"
import Icondateyesterday from "../assets/icondateyesterday.svg"
import Icondatetomorrow from "../assets/icondatetomorrow.svg"
import Iconcontainer from "../assets/iconcontainer.svg"
import Iconcontainer1 from "../assets/iconcontainer.svg"
import Iconcontainer2 from "../assets/iconcontainer.svg"
import Iconcontainer3 from "../assets/iconcontainer.svg"
import Ellipse1 from "../assets/ellipse-1.svg"
import Ellipse2 from "../assets/ellipse-2.svg"
import Ellipse3 from "../assets/ellipse-3.svg"
import Navihome from "../assets/navihome.svg"
import Navical from "../assets/navical.svg"
import Navidailysolid from "../assets/navidailysolid.svg"
import Navireport from "../assets/navireport.svg"
import Navimy from "../assets/navimy.svg"
import Cap from "../assets/cap.svg"
import Wifi from "../assets/wifi.svg"
import Cellularconnection from "../assets/cellular-connection.svg"
import { Color, Gap, FontFamily, FontSize, Border, Padding } from "../GlobalStyles";

const Component = () => {
  	
  	return (
    		<SafeAreaView style={styles.safeareaview}>
      			<View style={styles.view}>
        				<View style={styles.header}>
          					<View style={[styles.headeritem, styles.routinecheckminiFlexBox]}>
            						<View style={[styles.logocontainer, styles.logocontainerFlexBox]}>
              							<Image style={styles.logoIcon} resizeMode="cover" source="logo.png" />
              							<Text style={[styles.text, styles.textTypo2]}>위케어</Text>
            						</View>
            						<View style={styles.container}>
              							<Iconbell style={styles.iconbell} width={24} height={24} />
              							<Iconplus style={styles.iconbell} width={24} height={24} />
            						</View>
          					</View>
        				</View>
        				<View style={[styles.personcardParent, styles.logocontainerFlexBox]}>
          					<View style={styles.personcard}>
            						<View style={styles.cardbody}>
              							<View style={styles.profile}>
                								<View style={[styles.profileimage, styles.borderPosition1]} />
                								<View style={[styles.profiletext, styles.borderPosition1]}>
                  									<View style={styles.nametextcontainer}>
                    										<Text style={[styles.text, styles.textTypo2]}>피보호자</Text>
                  									</View>
                  									<View style={styles.nametextcontainer1}>
                    										<Text style={[styles.text, styles.textTypo2]}>님</Text>
                  									</View>
                  									<View style={styles.nametextcontainer}>
                    										<Text style={[styles.text3, styles.textLayout]} />
                  									</View>
                								</View>
              							</View>
              							<View style={styles.datecontainer}>
                								<Icondateyesterday style={styles.iconbell} width={24} height={24} />
                								<View style={styles.frame}>
                  									<Text style={[styles.text4, styles.textTypo3]}>2025/07/10</Text>
                  									<Text style={[styles.text5, styles.textTypo1]}>오전 9:49</Text>
                								</View>
                								<Icondatetomorrow style={styles.iconbell} width={24} height={24} />
              							</View>
              							<View style={styles.routines}>
                								<View style={[styles.routinecheckmini, styles.btnctaFlexBox]}>
                  									<View style={styles.container}>
                    										<Iconcontainer style={[styles.iconcontainer, styles.iconcontainerLayout]} width={48} height={48} />
                    										<View>
                      											<Text style={[styles.text6, styles.textTypo2]}>혈압약 먹기</Text>
                      											<Text style={[styles.text7, styles.textTypo1]}>오전 10시 ~ 00시</Text>
                    										</View>
                  									</View>
                  									<View style={styles.buttonscheckbox} />
                								</View>
                								<View style={[styles.routinecheckmini1, styles.routinecheckminiFlexBox]}>
                  									<View style={styles.container}>
                    										<Iconcontainer1 style={[styles.iconcontainer1, styles.iconcontainerLayout]} width={48} height={48} />
                    										<View>
                      											<Text style={[styles.text6, styles.textTypo2]}>아침 먹기</Text>
                      											<Text style={[styles.text7, styles.textTypo1]}>오전 7시 ~ 12시</Text>
                    										</View>
                  									</View>
                  									<View style={styles.buttonscheckbox} />
                								</View>
                								<View style={[styles.routinecheckmini, styles.btnctaFlexBox]}>
                  									<View style={styles.container}>
                    										<Iconcontainer2 style={[styles.iconcontainer2, styles.iconcontainerLayout]} width={48} height={48} />
                    										<View>
                      											<Text style={[styles.text6, styles.textTypo2]}>산책 하기</Text>
                    										</View>
                  									</View>
                  									<View style={styles.buttonscheckbox} />
                								</View>
              							</View>
              							<View style={styles.addalramcontainer}>
                								<View style={styles.alramcontentcontainer}>
                  									<Iconcontainer3 style={styles.iconcontainer3} width={28} height={28} />
                  									<View>
                    										<Text style={styles.text11}>할 일 추가하기</Text>
                  									</View>
                								</View>
              							</View>
              							<View style={[styles.btncta, styles.btnctaFlexBox]}>
                								<Text style={styles.text12}>전체 할 일 보기</Text>
              							</View>
            						</View>
          					</View>
          					<View style={styles.slider}>
            						<Ellipse1 style={styles.sliderChild} width={12} height={12} />
            						<Ellipse2 style={styles.sliderChild} width={12} height={12} />
            						<Ellipse3 style={styles.sliderChild} width={12} height={12} />
          					</View>
        				</View>
        				<View style={[styles.navigation, styles.navigationPosition]}>
          					<View style={styles.navihome}>
            						<Navihome style={styles.iconbell} width={24} height={24} />
            						<View style={styles.navimenu}>
              							<Text style={[styles.text13, styles.textTypo]}>홈</Text>
            						</View>
          					</View>
          					<View style={styles.navihome}>
            						<Navical style={styles.iconbell} width={24} height={24} />
            						<View style={styles.navimenu}>
              							<Text style={[styles.text14, styles.textPosition]}>일정</Text>
            						</View>
          					</View>
          					<View style={styles.navihome}>
            						<Navidailysolid style={styles.iconbell} width={24} height={24} />
            						<View style={styles.navimenu}>
              							<Text style={[styles.text15, styles.textPosition]}>하루</Text>
            						</View>
          					</View>
          					<View style={styles.navihome}>
            						<Navireport style={styles.iconbell} width={24} height={24} />
            						<View style={styles.navimenu}>
              							<Text style={[styles.text16, styles.textTypo]}>리포트</Text>
            						</View>
          					</View>
          					<View style={styles.navihome}>
            						<Navimy style={styles.iconbell} width={24} height={24} />
            						<View style={styles.navimenu}>
              							<Text style={[styles.text14, styles.textPosition]}>마이</Text>
            						</View>
          					</View>
        				</View>
        				<View style={styles.statusBar}>
          					<View style={[styles.time, styles.timePosition]}>
            						<Text style={styles.time1}>9:41</Text>
          					</View>
          					<View style={[styles.levels, styles.timePosition]}>
            						<View style={[styles.battery, styles.borderPosition]}>
              							<View style={[styles.border, styles.borderPosition]} />
              							<Cap style={[styles.capIcon, styles.iconPosition]} />
              							<View style={[styles.capacity, styles.capacityPosition]} />
            						</View>
            						<Wifi style={[styles.wifiIcon, styles.iconPosition]} />
            						<Cellularconnection style={[styles.cellularConnectionIcon, styles.iconPosition]} />
          					</View>
        				</View>
        				<View style={[styles.homeIndicator, styles.navigationPosition]}>
          					<View style={[styles.homeIndicator1, styles.capacityPosition]} />
        				</View>
      			</View>
    		</SafeAreaView>);
};

const styles = StyleSheet.create({
  	safeareaview: {
    		backgroundColor: Color.colorWhitesmoke,
    		flex: 1
  	},
  	routinecheckminiFlexBox: {
    		gap: 0,
    		justifyContent: "space-between"
  	},
  	logocontainerFlexBox: {
    		gap: Gap.gap_6,
    		alignItems: "center"
  	},
  	textTypo2: {
    		textAlign: "left",
    		color: Color.colorBlack,
    		fontFamily: FontFamily.pretendard,
    		fontWeight: "700"
  	},
  	borderPosition1: {
    		bottom: "0%",
    		top: "0%",
    		height: "100%"
  	},
  	textLayout: {
    		lineHeight: 29,
    		fontSize: FontSize.size_18
  	},
  	textTypo3: {
    		color: Color.colorBlack,
    		textAlign: "center",
    		fontFamily: FontFamily.pretendard
  	},
  	textTypo1: {
    		color: Color.colorDimgray,
    		textAlign: "center",
    		fontFamily: FontFamily.pretendard
  	},
  	btnctaFlexBox: {
    		width: 276,
    		alignItems: "center",
    		flexDirection: "row"
  	},
  	iconcontainerLayout: {
    		height: 48,
    		width: 48
  	},
  	navigationPosition: {
    		width: 393,
    		backgroundColor: Color.colorWhite,
    		left: 0,
    		position: "absolute"
  	},
  	textTypo: {
    		lineHeight: 16,
    		color: Color.colorMidnightblue,
    		fontSize: FontSize.size_16,
    		textAlign: "center",
    		top: "0%",
    		fontFamily: FontFamily.pretendard,
    		position: "absolute"
  	},
  	textPosition: {
    		left: "13.33%",
    		lineHeight: 16,
    		fontSize: FontSize.size_16,
    		textAlign: "center",
    		top: "0%",
    		fontFamily: FontFamily.pretendard,
    		position: "absolute"
  	},
  	timePosition: {
    		top: "50%",
    		width: "35.75%",
    		marginTop: -27,
    		height: 54,
    		position: "absolute"
  	},
  	borderPosition: {
    		left: "50%",
    		position: "absolute"
  	},
  	iconPosition: {
    		maxHeight: "100%",
    		left: "50%",
    		position: "absolute"
  	},
  	capacityPosition: {
    		backgroundColor: Color.colorBlack,
    		left: "50%",
    		position: "absolute"
  	},
  	logoIcon: {
    		width: 32,
    		height: 32,
    		opacity: 0.77,
    		borderRadius: Border.br_24
  	},
  	text: {
    		lineHeight: 38,
    		fontSize: FontSize.size_24,
    		textAlign: "left"
  	},
  	logocontainer: {
    		flexDirection: "row"
  	},
  	iconbell: {
    		width: 24,
    		height: 24,
    		overflow: "hidden"
  	},
  	container: {
    		gap: Gap.gap_20,
    		alignItems: "center",
    		flexDirection: "row"
  	},
  	headeritem: {
    		width: 353,
    		alignItems: "center",
    		flexDirection: "row"
  	},
  	header: {
    		top: 54,
    		shadowColor: Color.colorGray300,
    		shadowOffset: {
      			width: 0,
      			height: 2
    		},
    		shadowRadius: 4,
    		elevation: 4,
    		height: 66,
    		paddingHorizontal: Padding.p_20,
    		paddingVertical: 14,
    		alignItems: "center",
    		flexDirection: "row",
    		backgroundColor: Color.colorLavender,
    		left: 0,
    		shadowOpacity: 1,
    		position: "absolute"
  	},
  	profileimage: {
    		width: "23.15%",
    		right: "0.01%",
    		left: "76.83%",
    		borderRadius: 999,
    		backgroundColor: Color.colorGray100,
    		position: "absolute",
    		overflow: "hidden"
  	},
  	nametextcontainer: {
    		justifyContent: "center",
    		alignSelf: "stretch",
    		alignItems: "center",
    		flexDirection: "row"
  	},
  	nametextcontainer1: {
    		justifyContent: "center",
    		height: 60,
    		alignItems: "center",
    		flexDirection: "row"
  	},
  	text3: {
    		textAlign: "center",
    		color: Color.colorBlack,
    		fontFamily: FontFamily.pretendard
  	},
  	profiletext: {
    		width: "61.38%",
    		right: "38.62%",
    		gap: 2,
    		left: "0%",
    		alignItems: "center",
    		flexDirection: "row",
    		position: "absolute"
  	},
  	profile: {
    		height: 60,
    		alignSelf: "stretch"
  	},
  	text4: {
    		fontSize: 20,
    		lineHeight: 32,
    		textAlign: "center",
    		fontWeight: "700",
    		color: Color.colorBlack
  	},
  	text5: {
    		fontSize: 15,
    		lineHeight: 24,
    		alignSelf: "stretch"
  	},
  	frame: {
    		gap: 1,
    		alignItems: "center"
  	},
  	datecontainer: {
    		gap: 50,
    		alignSelf: "stretch",
    		alignItems: "center",
    		flexDirection: "row"
  	},
  	iconcontainer: {
    		borderRadius: Border.br_24,
    		overflow: "hidden"
  	},
  	text6: {
    		fontSize: FontSize.size_22,
    		lineHeight: 35
  	},
  	text7: {
    		lineHeight: 29,
    		fontSize: FontSize.size_18
  	},
  	buttonscheckbox: {
    		width: 36,
    		borderRadius: Border.br_6,
    		borderColor: Color.colorLightgray,
    		borderWidth: 3,
    		height: 36,
    		borderStyle: "solid",
    		overflow: "hidden",
    		backgroundColor: Color.colorWhitesmoke
  	},
  	routinecheckmini: {
    		gap: 0,
    		justifyContent: "space-between"
  	},
  	iconcontainer1: {
    		borderRadius: 56
  	},
  	routinecheckmini1: {
    		alignSelf: "stretch",
    		alignItems: "center",
    		flexDirection: "row"
  	},
  	iconcontainer2: {
    		borderRadius: 60
  	},
  	routines: {
    		gap: 18
  	},
  	iconcontainer3: {
    		width: 28,
    		borderRadius: Border.br_100,
    		height: 28
  	},
  	text11: {
    		lineHeight: 26,
    		fontSize: FontSize.size_16,
    		textAlign: "left",
    		color: Color.colorBlack,
    		fontFamily: FontFamily.pretendard,
    		fontWeight: "700"
  	},
  	alramcontentcontainer: {
    		gap: 16,
    		alignItems: "center",
    		flexDirection: "row"
  	},
  	addalramcontainer: {
    		paddingLeft: Padding.p_10,
    		height: 28,
    		alignSelf: "stretch",
    		alignItems: "center",
    		flexDirection: "row"
  	},
  	text12: {
    		color: Color.colorWhite,
    		textAlign: "center",
    		fontFamily: FontFamily.pretendard,
    		fontWeight: "700",
    		lineHeight: 38,
    		fontSize: FontSize.size_24
  	},
  	btncta: {
    		backgroundColor: Color.colorMediumslateblue,
    		paddingHorizontal: Padding.p_30,
    		paddingVertical: Padding.p_10,
    		justifyContent: "center",
    		borderRadius: Border.br_10,
    		width: 276
  	},
  	cardbody: {
    		gap: 26,
    		alignSelf: "stretch",
    		alignItems: "center"
  	},
  	personcard: {
    		shadowColor: Color.colorDarkgray,
    		shadowOffset: {
      			width: 0,
      			height: 4
    		},
    		shadowRadius: 20,
    		elevation: 20,
    		paddingLeft: 38,
    		paddingTop: Padding.p_30,
    		paddingRight: 39,
    		paddingBottom: Padding.p_30,
    		backgroundColor: Color.colorWhite,
    		borderRadius: Border.br_10,
    		alignSelf: "stretch",
    		alignItems: "center",
    		shadowOpacity: 1
  	},
  	sliderChild: {
    		width: 12,
    		height: 12
  	},
  	slider: {
    		gap: 15,
    		alignItems: "center",
    		flexDirection: "row"
  	},
  	personcardParent: {
    		top: 134,
    		left: 20,
    		width: 353,
    		position: "absolute"
  	},
  	text13: {
    		left: "33.33%",
    		color: Color.colorMidnightblue
  	},
  	navimenu: {
    		width: 40,
    		height: 16
  	},
  	navihome: {
    		paddingHorizontal: Padding.p_8,
    		paddingVertical: Padding.p_4,
    		gap: Gap.gap_8,
    		justifyContent: "center",
    		alignSelf: "stretch",
    		alignItems: "center",
    		flex: 1
  	},
  	text14: {
    		color: Color.colorMidnightblue
  	},
  	text15: {
    		fontWeight: "800",
    		color: Color.colorMediumslateblue
  	},
  	text16: {
    		left: "-4.17%",
    		color: Color.colorMidnightblue
  	},
  	navigation: {
    		top: 746,
    		shadowColor: Color.colorGray200,
    		shadowOffset: {
      			width: 0,
      			height: -1
    		},
    		shadowRadius: 5,
    		elevation: 5,
    		paddingTop: Padding.p_10,
    		paddingBottom: Padding.p_20,
    		alignItems: "center",
    		flexDirection: "row",
    		shadowOpacity: 1,
    		width: 393
  	},
  	time1: {
    		top: "33.96%",
    		left: "36.96%",
    		lineHeight: 22,
    		fontWeight: "600",
    		fontFamily: FontFamily.pretendard,
    		textAlign: "center",
    		fontSize: FontSize.size_18,
    		color: Color.colorBlack,
    		position: "absolute"
  	},
  	time: {
    		right: "64.25%",
    		left: "0%"
  	},
  	border: {
    		marginLeft: -13.65,
    		borderRadius: 4,
    		borderColor: Color.colorBlack,
    		borderWidth: 1,
    		width: 25,
    		opacity: 0.35,
    		borderStyle: "solid",
    		bottom: "0%",
    		top: "0%",
    		height: "100%"
  	},
  	capIcon: {
    		height: "31.54%",
    		marginLeft: 12.35,
    		top: "36.78%",
    		bottom: "31.68%",
    		width: 1,
    		opacity: 0.4
  	},
  	capacity: {
    		height: "69.23%",
    		marginLeft: -11.65,
    		top: "15.38%",
    		bottom: "15.38%",
    		borderRadius: 3,
    		width: 21
  	},
  	battery: {
    		height: "24.07%",
    		marginLeft: 10.75,
    		top: "42.59%",
    		bottom: "33.33%",
    		width: 27
  	},
  	wifiIcon: {
    		height: "22.78%",
    		marginLeft: -13.55,
    		top: "43.77%",
    		bottom: "33.45%",
    		width: 17
  	},
  	cellularConnectionIcon: {
    		height: "22.59%",
    		marginLeft: -40.25,
    		top: "43.58%",
    		bottom: "33.82%",
    		width: 19
  	},
  	levels: {
    		right: "0%",
    		left: "64.25%"
  	},
  	statusBar: {
    		top: 0,
    		height: 54,
    		width: 393,
    		backgroundColor: Color.colorLavender,
    		left: 0,
    		position: "absolute"
  	},
  	homeIndicator1: {
    		marginLeft: 69.5,
    		bottom: 8,
    		width: 139,
    		height: 5,
    		transform: [
      			{
        				rotate: "180deg"
      			}
    		],
    		borderRadius: Border.br_100
  	},
  	homeIndicator: {
    		bottom: 0,
    		height: 20
  	},
  	view: {
    		width: "100%",
    		height: 852,
    		overflow: "hidden",
    		backgroundColor: Color.colorWhitesmoke,
    		flex: 1
  	}
});

export default Component;
