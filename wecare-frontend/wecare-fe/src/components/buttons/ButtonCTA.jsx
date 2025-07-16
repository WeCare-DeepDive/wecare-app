/*
 */
import * as React from "react";
import {StyleSheet, Text, View, Image} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import Pictureframe from "../assets/pictureframe.svg"

const Component = () => {
  	
  	return (
    		<SafeAreaView style={styles.viewBg}>
      			<View style={[styles.view, styles.viewBg]}>
        				<View style={[styles.invitetext, styles.batteryPosition]}>
          					<View style={styles.pictureframeParent}>
            						<Pictureframe style={styles.pictureframeIcon} width={120} height={120} />
            						<Text style={styles.text}>케어를 함께할 가족을 초대해 보세요!</Text>
          					</View>
          					<View style={[styles.btncta, styles.btnctaFlexBox]}>
            						<Text style={[styles.text1, styles.textTypo1]}>초대하기</Text>
          					</View>
        				</View>
        				<View style={styles.header}>
          					<View style={styles.headeritem}>
            						<View style={styles.logocontainer}>
              							<Image style={styles.logoIcon} resizeMode="cover" source="logo.png" />
              							<Text style={[styles.text2, styles.textTypo1]}>위케어</Text>
            						</View>
            						<View style={styles.container}>
              							<Pictureframe style={styles.iconbell} width={24} height={24} />
              							<Pictureframe style={styles.iconbell} width={24} height={24} />
            						</View>
          					</View>
        				</View>
        				<View style={[styles.navigation, styles.navigationPosition]}>
          					<View style={[styles.navihome, styles.btnctaFlexBox]}>
            						<Pictureframe style={styles.iconbell} width={24} height={24} />
            						<View style={styles.navimenu}>
              							<Text style={[styles.text3, styles.textTypo]}>홈</Text>
            						</View>
          					</View>
          					<View style={[styles.navihome, styles.btnctaFlexBox]}>
            						<Pictureframe style={styles.iconbell} width={24} height={24} />
            						<View style={styles.navimenu}>
              							<Text style={[styles.text4, styles.textPosition]}>일정</Text>
            						</View>
          					</View>
          					<View style={[styles.navihome, styles.btnctaFlexBox]}>
            						<Pictureframe style={styles.iconbell} width={24} height={24} />
            						<View style={styles.navimenu}>
              							<Text style={[styles.text5, styles.textPosition]}>하루</Text>
            						</View>
          					</View>
          					<View style={[styles.navihome, styles.btnctaFlexBox]}>
            						<Pictureframe style={styles.iconbell} width={24} height={24} />
            						<View style={styles.navimenu}>
              							<Text style={[styles.text6, styles.textTypo]}>리포트</Text>
            						</View>
          					</View>
          					<View style={[styles.navihome, styles.btnctaFlexBox]}>
            						<Pictureframe style={styles.iconbell} width={24} height={24} />
            						<View style={styles.navimenu}>
              							<Text style={[styles.text4, styles.textPosition]}>마이</Text>
            						</View>
          					</View>
        				</View>
        				<View style={[styles.homeIndicator, styles.navigationPosition]}>
          					<View style={[styles.homeIndicator1, styles.capacityPosition]} />
        				</View>
        				<View style={styles.statusBar}>
          					<View style={[styles.time, styles.timePosition]}>
            						<Text style={styles.time1}>9:41</Text>
          					</View>
          					<View style={[styles.levels, styles.timePosition]}>
            						<View style={[styles.battery, styles.batteryPosition]}>
              							<View style={styles.border} />
              							<Pictureframe style={[styles.capIcon, styles.iconPosition]} />
              							<View style={[styles.capacity, styles.capacityPosition]} />
            						</View>
            						<Pictureframe style={[styles.wifiIcon, styles.iconPosition]} />
            						<Pictureframe style={[styles.cellularConnectionIcon, styles.iconPosition]} />
          					</View>
        				</View>
      			</View>
    		</SafeAreaView>);
};

const styles = StyleSheet.create({
  	safeareaview: {
    		flex: 1,
    		backgroundColor: "#fff"
  	},
  	viewBg: {
    		backgroundColor: "#fff",
    		flex: 1
  	},
  	batteryPosition: {
    		left: "50%",
    		position: "absolute"
  	},
  	btnctaFlexBox: {
    		justifyContent: "center",
    		alignItems: "center"
  	},
  	textTypo1: {
    		fontWeight: "700",
    		fontFamily: "NanumSquareRoundOTF"
  	},
  	navigationPosition: {
    		width: 393,
    		left: 0,
    		position: "absolute",
    		backgroundColor: "#fff"
  	},
  	textTypo: {
    		lineHeight: 16,
    		fontSize: 16,
    		color: "#001d6c",
    		top: "0%",
    		textAlign: "center",
    		fontFamily: "NanumSquareRoundOTF",
    		position: "absolute"
  	},
  	textPosition: {
    		left: "13.33%",
    		lineHeight: 16,
    		fontSize: 16,
    		top: "0%",
    		textAlign: "center",
    		fontFamily: "NanumSquareRoundOTF",
    		position: "absolute"
  	},
  	capacityPosition: {
    		backgroundColor: "#000",
    		left: "50%",
    		position: "absolute"
  	},
  	timePosition: {
    		width: "35.75%",
    		marginTop: -27,
    		height: 54,
    		top: "50%",
    		position: "absolute"
  	},
  	iconPosition: {
    		maxHeight: "100%",
    		left: "50%",
    		position: "absolute"
  	},
  	pictureframeIcon: {
    		borderRadius: 190
  	},
  	text: {
    		textAlign: "center",
    		color: "#000",
    		fontFamily: "NanumSquareRoundOTF",
    		lineHeight: 32,
    		fontSize: 20
  	},
  	pictureframeParent: {
    		gap: 12,
    		alignSelf: "stretch",
    		alignItems: "center"
  	},
  	text1: {
    		color: "#fff",
    		textAlign: "center",
    		lineHeight: 32,
    		fontSize: 20,
    		fontWeight: "700"
  	},
  	btncta: {
    		borderRadius: 10,
    		backgroundColor: "#7777ff",
    		paddingHorizontal: 30,
    		paddingVertical: 10,
    		flexDirection: "row"
  	},
  	invitetext: {
    		marginTop: -123,
    		marginLeft: -149.5,
    		width: 299,
    		gap: 30,
    		alignItems: "center",
    		top: "50%",
    		left: "50%"
  	},
  	logoIcon: {
    		width: 32,
    		borderRadius: 24,
    		height: 32,
    		opacity: 0.77
  	},
  	text2: {
    		fontSize: 24,
    		lineHeight: 38,
    		textAlign: "left",
    		color: "#000"
  	},
  	logocontainer: {
    		gap: 6,
    		flexDirection: "row",
    		alignItems: "center"
  	},
  	iconbell: {
    		overflow: "hidden"
  	},
  	container: {
    		gap: 20,
    		flexDirection: "row",
    		alignItems: "center"
  	},
  	headeritem: {
    		width: 353,
    		justifyContent: "space-between",
    		gap: 0,
    		flexDirection: "row",
    		alignItems: "center"
  	},
  	header: {
    		top: 54,
    		shadowColor: "rgba(0, 0, 0, 0.08)",
    		shadowOffset: {
      			width: 0,
      			height: 2
    		},
    		shadowRadius: 4,
    		elevation: 4,
    		height: 66,
    		paddingHorizontal: 20,
    		paddingVertical: 14,
    		backgroundColor: "#d6d8ff",
    		left: 0,
    		shadowOpacity: 1,
    		flexDirection: "row",
    		alignItems: "center",
    		position: "absolute"
  	},
  	text3: {
    		left: "33.33%",
    		color: "#001d6c"
  	},
  	navimenu: {
    		width: 40,
    		height: 16
  	},
  	navihome: {
    		paddingHorizontal: 8,
    		paddingVertical: 4,
    		gap: 8,
    		alignSelf: "stretch",
    		flex: 1,
    		justifyContent: "center"
  	},
  	text4: {
    		color: "#001d6c"
  	},
  	text5: {
    		fontWeight: "800",
    		color: "#7777ff"
  	},
  	text6: {
    		left: "-4.17%",
    		color: "#001d6c"
  	},
  	navigation: {
    		top: 746,
    		shadowColor: "rgba(0, 0, 0, 0.15)",
    		shadowOffset: {
      			width: 0,
      			height: -1
    		},
    		shadowRadius: 5,
    		elevation: 5,
    		paddingTop: 10,
    		paddingBottom: 20,
    		shadowOpacity: 1,
    		width: 393,
    		flexDirection: "row",
    		alignItems: "center"
  	},
  	homeIndicator1: {
    		marginLeft: 69.5,
    		bottom: 8,
    		borderRadius: 100,
    		width: 139,
    		height: 5,
    		transform: [
      			{
        				rotate: "180deg"
      			}
    		]
  	},
  	homeIndicator: {
    		bottom: 0,
    		height: 20
  	},
  	time1: {
    		top: "33.96%",
    		left: "36.96%",
    		fontSize: 18,
    		lineHeight: 22,
    		fontWeight: "600",
    		fontFamily: "Pretendard",
    		textAlign: "center",
    		color: "#000",
    		position: "absolute"
  	},
  	time: {
    		right: "64.25%",
    		left: "0%"
  	},
  	border: {
    		height: "100%",
    		marginLeft: -13.65,
    		bottom: "0%",
    		borderRadius: 4,
    		borderStyle: "solid",
    		borderColor: "#000",
    		borderWidth: 1,
    		width: 25,
    		opacity: 0.35,
    		top: "0%",
    		left: "50%",
    		position: "absolute"
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
    		backgroundColor: "#d6d8ff",
    		left: 0,
    		position: "absolute"
  	},
  	view: {
    		width: "100%",
    		height: 852,
    		overflow: "hidden"
  	}
});

export default Component;
