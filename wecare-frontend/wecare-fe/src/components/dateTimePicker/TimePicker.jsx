import React, { useState } from 'react'
import DatePicker from 'react-native-date-picker'
import { StyleSheet } from 'react-native'   
import { Colors, FontFamily, FontSize } from '../../styles/theme'


export default function TimePicker() {
    const [date, setDate] = useState(new Date());
    
    return <DatePicker mode='time' date={date} onDateChange={setDate}  />
}
