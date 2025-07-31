import { Calendar, LocaleConfig } from "react-native-calendars"
import { Theme } from "../../../styles/theme"
const FullCalendar = () => {
    const nowDate = new Date();
    return (
        <Calendar 
            //현재 날짜 설정
            current={nowDate.toDateString()}
            //캘린더 날짜 선택 설정
            onDayPress={(day) => {
                console.log('Selected day:', day);
            }}
            //한글 설정
            locale="ko"
            // firstDay=1이면 월요일 부터 시작, 0이면 일요일 부터 시작
            firstDay={1}
            mounthFormat={'MM월'}
            hideExtraDays={false}
            // 디자인 수정 필요함
            theme={{
                calendarBackground: Theme.Colors.colorWhite,
                textSectionTitleColor: Theme.Colors.colorBlack,
                selectedDayBackgroundColor: Theme.Colors.colorBlack,
                selectedDayTextColor: Theme.Colors.colorWhite,
                todayTextColor: Theme.Colors.colorBlack,
                dayTextColor: Theme.Colors.colorBlack,
                textDayFontFamily: Theme.FontFamily.pretendard,
                textMonthFontFamily: Theme.FontFamily.pretendard,
                textDayHeaderFontFamily: Theme.FontFamily.nanumB,
                textDayFontWeight: '400',
                textMonthFontWeight: '400',
                textDayHeaderFontWeight: '400',
                // size 나중에 스타일 확인하면서 상단에 변수로 치환 해야함
                textDayFontSize: Theme.FontSize.size_12,
                textMonthFontSize: Theme.FontSize.size_12,
                textDayHeaderFontSize: Theme.FontSize.size_12,
                textDayStyle: {
                    fontSize: Theme.FontSize.size_12,
                }
            }}
        />
    )
}

LocaleConfig.locales.ko = {
    monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
    dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
    today: '오늘',
}

LocaleConfig.defaultLocale = 'ko';

export default FullCalendar;
