package tools.mailer.di.anntation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // アノテーションの情報をクラス・ファイルに出力し、実行時JVMにもロードされる。その為、リフレクションAPI経由でその情報にアクセスできる。
@Target(ElementType.TYPE)           // クラス宣言、インタフェース宣言、アノテーション型宣言、enum宣言のみに使える
public @interface Plugin {
}
