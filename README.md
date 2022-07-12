# 숫자 맞추기

## 앱 소개
### 8쌍의 카드 중 상대방보다 더 많은 쌍을 찾으세요!

## Stack

>> ### Front
>>  * Language: Kotlin
>>  * IDE: Android Studio
>> ### Back
>>  * Language: Javascript
>>  * IDE: Vim
>>  * Database: MySQL

## Splash
>> <img src="https://user-images.githubusercontent.com/52152015/178481306-c406e262-5fa3-4674-a7df-bb2d25807d24.gif" width="50%" />
앱을 실행시킬 때의 애니메이션입니다.

## Authorization
>> <img src="https://user-images.githubusercontent.com/52152015/178481791-d3f3f873-6383-4e4e-82ab-d993062c0594.png" width="30%" />
>> <img src="https://user-images.githubusercontent.com/52152015/178482034-d67b40a7-c42b-4e7d-943a-ec21c7419960.png" width="30%" />

로컬 로그인으로 구현.
JWT과 SharedPreference를 통해 앱을 종료하였다가 다시 실행하더라도 로그인 유지.

## Main
>> <img src="https://user-images.githubusercontent.com/52152015/178482486-381f6211-c5b3-435f-9aac-8619f63f4dc0.png" width="30%" />

로그인이 되어 있을 경우에만 유저 목록이 보임.
또한 현재 접속 중인 유저는 녹색 버튼이 활성화가 됨.

## User infromation
>> <img src="https://user-images.githubusercontent.com/52152015/178482580-738679d0-c9ba-4b96-a6d0-9d27157b9372.png" width="30%" />
>> <img src="https://user-images.githubusercontent.com/52152015/178482591-78f0b0ea-ce43-46c7-aa15-e90fde0d8f28.png" width="30%" />


자신의 정보 페이지일 경우, 대결 버튼이 보이지 않지만, 접속 중인 상대방의 경우에는 대결 버튼 활성화

대결 버튼 클릭 시, 상대방은 메인화면에서 게임을 수락할 수 있음.

## Game
>> <img src="https://user-images.githubusercontent.com/52152015/178482792-063982d3-bea0-4b3e-a6e5-41f2c767f057.png" width="30%" />
>> <img src="https://user-images.githubusercontent.com/52152015/178482801-3ca008fe-920e-44a2-99be-a893bd604ceb.png" width="30%" />

상대방이 게임 룸에 입장하면 상대방의 턴으로 게임 시작. 누구의 턴인지는 위의 이름에 빨간색 테두리가 있는지로 확인할 수 있음.

2 장의 카드를 뒤집었을 때, 같은 숫자면 1점을 얻고 계속 진행하고, 다른 숫자면 상대방에게 턴이 넘어감.

모든 카드를 뒤집었을 때, 점수가 더 높은 사람이 승리. 점수가 같다면 비김.
