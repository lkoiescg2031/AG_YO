package e.lkoie.ag_yo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private SharedPreferences prof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prof = getSharedPreferences("Version", Activity.MODE_PRIVATE);
        if(prof.getString("version",null) != null)
                directActivity();
        btn = (Button) findViewById(R.id.main_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSampleJoke();
                prof.edit().putString("version","1").commit();
                directActivity();
            }
        });
    }

    //SampleDataInitializer
    private void makeSampleJoke(){
        DBController dbController = new DBController(this);
        String sampleData =
                new StringBuilder("1.미소의 반댓말은?_당기소_2.중학생과 고등학생이 타는 차는?_중고차_3.김은 김인데 하얀 김은?_앙드레 김_6.세사람만 탈수있는 차는?_인삼차_15.슈렉의 어머니는?_녹색어머니_18.우리나라 최초의 다이빙선수는?_심청이_22.머리로 들어가고 입으로 나오는 것은?_주전자_24.개미네 주소는?")
                        .append("_허리도가늘군만지면부서지리_25.타이타닉의 구명보트에 몇 명이 탈 수 있을까요?_9명_26.창문이 100개 였는데 2개가 깨지면?_window 98_29.신사가 하는 인사는?_신사임당_30.할아버지가 좋아하는 돈은?_할머니_33.왕이 넘어지면?_킹콩_38.남녀가 자고 나면 생기는 것은?_눈꼽_40.엄마가 길을 잃으면?")
                        .append("_맘마미아_41.맞을수록 기분 좋은 것은?_시험문제_42.동생이 형을 굉장히 좋아합니다. 이것을 뭐라고 할까요?_형광펜_50.불은 불인데 안 뜨거운 불은?_이불_52.깨진 독을 고쳐주는 사람들은?_독수리오형제_53.세상에서 가장 큰 코는?_멕시코_54.세상에서 가장 큰 컵은?_월드컵_55.때리기를 잘하기로 소문난 깡패는?_펠레_")
                        .append("57.가장 더러운 강은?_요강_72.전주비빕밥보다 늦은 비빕밥은?_이번주비빕밥_73.세상에서 가장 먼저 자는 자는 사람은?_이미자_78.어부들이 가장 싫어하는 가수는?_배철수_85. 오리를 생으로 먹으면?_회오리_86. 흑인이 시험을 보면?_검정고시_88. 원숭이를 불에 구우면?_구운몽_91. 신발이 화가나면?_신발끈_")
                        .append("3. 서울 시민 모두가 동시에 외치면 무슨 말이 될까?_천만의 말씀_4. 금는 금인데 도둑 고양이에게 가장 잘 어울리는 금은?_야금야금_5. 고기 먹을 때마다 따라오는 개는?_이쑤시개_17. ˝개가 사람을 가르친다˝를 4자로 줄이면?_개인지도_18. ˝낮선 여자에서 그 남자의 향기를 느꼈다˝를 5자로 줄이면?_혹시이년이_")
                        .append("20. 쥐가 네 마리 모이면 무엇이 될까?_쥐포_21. 소금을 죽이면 무엇이 될까?_죽염_23. 애 낳다가 죽은 여자를 4자로 줄이면?_다이에나_24. ˝당신은 시골에 삽니다˝를 3자로 줄이면?_유인촌_25. 콜라와 마요네즈를 섞으면 어떻게 되는가?_못먹게된다_26. 곤충의 몸을 3등분하면?_죽는다_27. 무엇이든지 혼자 다 해먹는 사람은?_자취생_")
                        .append("8. 애들이 학교에 가는 이유는?_학교가올수없으니까_29. 청량리 588, 미아리 택사스촌 등 창녀촌을 4자로 표현하면?_구멍가게_30. 63삘딩에서 떨어져도 살 수 있는 방법은?_1층에서뛰어내린다_33. ˝이것이 코다˝를 3자로 줄이면?_디스코_38. 하느님도 부처님도 싫어하는 비는?_사이비_40. 빚쟁이가 가장 좋아하는 병?_기억상실증_")
                        .append("54. 이 세상에서 제일가는 불효자는?_애밀졸라_67. 새중에서 가장 무서운 새는?_으악새_23. 운전수가 제일 싫어하는 춤은?_우선멈춤_14. 울다가 그친 사람을 다섯 글자로 하면?_아까운 사람_10. 동생과 형이 싸우는데 엄마가 동생 편을 드는 세상은?_형편없는세상_60. 아프지도 않는데 매일 사용하는 약은?")
                        .append("_치약_30. 식인종은 엘리베이터를 뭐라고 부를까?_자판기_92. 목욕탕 속에 들어있는 사람을 보고 식인종이 하는 말은?_국밥_30. 하늘을 마음대로 날아다니는 개는?_솔개_31.사람이 죽지 않는 산맥은?_안데스 산맥_32. 차를 건드리면?_카톡_33. 딸기가 직장을 잃으면?_딸기시럽_4. 왕이 집에 가기 싫으면?_궁시렁 궁시렁_")
                        .append("5. 바다에 사는 오리는?_미더덕_36. 사슴이 눈이 좋으면?_굿아이디어_37. 입이 s 자로 되어있으면?_EBS_8. 다리가 예쁘면?_다리미_39. 미국에 비가 오면?_USB_40. 기계로된 속옷은?_디지털프라자_41.대머리랑 연애하면 안되는 이유는?_헤어나올수없어서_42. 사과가 웃으면?_풋사과_43. 둘리가 다니는 고등학교는?_빙하타고_")
                        .append("44. 귀가 불에 타면?_타이어_14. 차가 놀라면?_카놀라유_5. 비가 한시간 동안 내리면?_추적60분_16. 덜 뚱뚱한 사람들이 모여 사는 동네는?_반포동_17. 세상에서 제일 가난한 왕은?_최저임금_18.우주인들이 술마시는 곳은?_스페이스바_19. 3월에 대학생들이 강한 이유는?_개강해서_20. 서울이 춥습니다를 다섯글자로 하면?_서울시립대_")
                        .append("21. 수박 한통에 오천원이면 두통에는?_게보린_22. 식인종이 우사인볼트를 보면?_패스트푸드_23.세상에서 가장 야한 음식은?_버섯_24. 사자를 끓이면?_동물의 왕국_25.맥주가 죽을때 남기는 말은?_유언비어_26. 돌잔치를 영어로 하면?_락페스티벌_27. 베를린에서 음식을 함부로 먹으면 안되는 이유는?_독일수도_")
                        .append("28. 밤에 성시경이 두명 있으면?_야간투시경_29. 신데렐라가 잠을 못자면?_모짜렐라_30. 엄마와 아들이 길을 걷다가 넘어지면?_모자이크_31. 모내기 기계가 고장나면?_심기불편_32. 자신의 엄마를 든사람이 노래를 부르면?_우리애미듬_33. 몽골인이 집이 마음에 들때 하는 말은?_게르만족_")
                        .append("34. 팩트만 말하는 오리는?_factory_35. 김태희가 신혼여행으로 간곳은?_비싼곳_").toString();
        dbController.openWrite();
        dbController.createTable(2);
        dbController.initialTable(sampleData);
        dbController.close();
    }
    private void directActivity(){
        Intent intent = new Intent(MainActivity.this, JokeListActivity.class);
        startActivity(intent);
        finish();
    }
}
