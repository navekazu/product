# DBConnector6について
JDBCを用いてデータベースに接続し、SQLを発行するためのJavaFXで実装したツールです。

***
---
___
# DBConnector6の特徴
DBConnector6の特徴を紹介します。

## 徹底したバックグラウンド実行
データベース接続後のメタ情報の取得やSQL実行結果の受け取りなど、データベースにリクエストして時間がかかる部分をすべてバックグラウンドで行うようにしています。  
そのため利用者が何かアクションをした後に当ツールが固まることなく、すぐに次のアクションを実行することが可能です。

## エビデンス取得支援機能
クエリ実行結果をクリップボードに貼り付けます。  
各カラムの区切り文字をタブ文字に設定することで、クエリ実行 → 実行結果のExcel貼り付けをスムーズに行うことが出来ます。

## 外部テキストエディタの呼び出し
当ツールのみでSQLクエリの編集は可能ですが、登録した外部テキストエディタで編集することも可能です。
使い慣れた外部テキストエディタで編集を行い、編集が終わったらテキストエディタを閉じることで、すぐに当ツールに編集内容を反映することが出来ます。

# ビルド方法
Mavenでビルドしてください。
```
mvn package
```

Windowsであればrebuild.batがあるので、こちらから clean -> package -> javadoc:javadoc がまとめて実行できます。 
```
rebuild.bat
```

# 起動方法
依存しているライブラリをすべて含んだJARファイルを作成していますので、下記コマンドで起動します。
```
java -jar target/DBConnector6-0.1-jar-with-dependencies.jar
```

Windowsであれば、スラッシュをバックスラッシュに読み替えてください。
```
java -jar target\DBConnector6-0.1-jar-with-dependencies.jar
```

# 操作方法

## データベース接続
![データベース接続画面](https://github.com/navekazu/product/raw/master/DBConnector6/img/01_Connect.png)  
起動直後にデータベース接続画面が開くので、そこに接続したいデータベースのパラメータを入力します。  
接続先の情報は以下の5つです。

|パラメータ    |情報                                           |
|--------------|-----------------------------------------------|
|Library path  |JDBCドライバのJARファイル                      |
|Driver        |JDBCドライバのクラス名                         |
|URL           |データベース接続の際のURL                      |
|User          |データベース接続の際のユーザ名（オプション）   |
|Password      |データベース接続の際のパスワード（オプション） |

起動のたびに上記を入力するのが手間の場合、画面中段の「Add」ボタンで入力内容を登録することが出来ます。  
登録すると上段の一覧に載るので、登録後は一覧から接続したいものを選択して「Load」ボタンのクリックで入力を省略することが出来ます。  
登録内容の更新は一覧の更新対象の行を選択した状態で「Update」ボタンをクリックし、登録内容の削除は「Delete」ボタンをクリックしてください。

入力内容が正しいか接続テストをする場合は、「Test」ボタンをクリックしてください。  
接続先の情報が入力できたら、「OK」ボタンをクリックしてください。

接続した内容は記録しているので、「History」から過去に接続した情報を引き出すことが出来ます。  
一覧に登録するまでもないが、再度入力するのが手間であれば「History」が便利です。


## メイン画面について
![メイン画面](https://github.com/navekazu/product/raw/master/DBConnector6/img/02_Main.png)  
データベースに接続すると、この画面に移ります。  
この画面で接続したデータベースに対して各種操作を行います。  
以下ではメイン画面のそれぞれのエリアについて説明します。

### データベース構造エリア
![メイン画面](https://github.com/navekazu/product/raw/master/DBConnector6/img/02_Main_01_Database.png)  
接続したデータベースの全スキーマをツリー表示します。  
スキーマを展開すると、そのスキーマに属するテーブルやビューやファンクション等を表示します。

スキーマを展開した際に表示される内容は、接続したデータベースプロダクトによって異なります。  
例えばOracleなら「シノニム、テーブル、ビュー、ファンクション、プロシージャ」ですし、PostgreSQLなら「テーブル、ビュー、プロシージャ、シーケンス、マテリアライズドビュー」等になります。

### テーブル構造エリア
![メイン画面](https://github.com/navekazu/product/raw/master/DBConnector6/img/02_Main_02_Table.png)  
データベース構造エリアで選択したテーブルやビューの

### クエリ入力エリア
![メイン画面](https://github.com/navekazu/product/raw/master/DBConnector6/img/02_Main_03_Query.png)

### 実行結果表示エリア
![メイン画面](https://github.com/navekazu/product/raw/master/DBConnector6/img/02_Main_04_Result.png)

### ログ出力エリア
![メイン画面](https://github.com/navekazu/product/raw/master/DBConnector6/img/02_Main_05_Log.png)

