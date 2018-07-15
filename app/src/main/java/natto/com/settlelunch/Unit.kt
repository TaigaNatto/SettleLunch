package natto.com.settlelunch

public class Unit {
    private val SELECT_FOODS= arrayOf(
            "カレー","ハヤシライス","ライス",//カレー
            "唐揚げ","そば","うどん",//定食
            "カツ丼","親子丼","魚",
            "チーズ","パスタ","ピザ",//イタリアン
            "コロッケ","ハンバーグ","オムライス",//洋食
            "エビチリ","麻婆豆腐","炒飯",//中華
            "ハンバーガー","牛丼","ラーメン")//その他

    private val FOOD_CATEGORIES= arrayOf(
            "カレー","定食","定食","イタリアン","洋食","中華","ハンバーガー","牛丼","ラーメン"
    )

    public fun getAllFoods():Array<String>{
        return this.SELECT_FOODS
    }

    public fun getFood(index:Int):String{
        return this.SELECT_FOODS[index]
    }

    public fun getCategory(foodIndex:Int):String{
        if(foodIndex/3>5){
            return this.SELECT_FOODS[foodIndex]
        }else{
            return this.FOOD_CATEGORIES[foodIndex/3]
        }
    }
}