import { useState } from 'react';

export const useFormattedBirthDate = () => {
  const [formattedValue, setFormattedValue] = useState('');

  const formatDate = (input) => {
    // 숫자만 추출
    const numbersOnly = input.replace(/\D/g, '');

    let formatted = numbersOnly;
    if (numbersOnly.length >= 5) {
      formatted = `${numbersOnly.slice(0, 4)}.${numbersOnly.slice(4, 6)}`;
    }
    if (numbersOnly.length >= 7) {
      formatted += `.${numbersOnly.slice(6, 8)}`;
    }

    setFormattedValue(formatted);
  };

  return {
    formattedValue,
    setFormattedValue: formatDate,
  };
};
