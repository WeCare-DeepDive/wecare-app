// mocks/getUserInfoMock.js (테스트용)
export const getUserInfoMockUnconnected = () => {
    return Promise.resolve({
      id: 1,
      username: 'guardian_user',
      name: '김보호',
      gender: 'MALE',
      birthDate: '1980-01-01',
      role: 'GUARDIAN',
      guardians: null,
      dependents: null,
    });
  };
  

  export const getUserInfoMock = () => {
    return Promise.resolve({
      id: 1,
      username: 'guardian_user',
      name: '김보호',
      gender: 'MALE',
      birthDate: '1980-01-01',
      role: 'GUARDIAN',
      guardians: null,
      dependents: [
        {
          id: 2,
          username: 'dependent_user1',
          name: '이피보',
          gender: 'FEMALE',
          birthDate: '2010-05-10',
          role: 'DEPENDENT',
          isActive: true,
        },
      ],
    });
  };
  