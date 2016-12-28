package tools.mailer.di.anntation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // アノテーションの情報をクラス・ファイルに出力し、実行時JVMにもロードされる。その為、リフレクションAPI経由でその情報にアクセスできる。
@Target(ElementType.METHOD)         // メソッド宣言
public @interface Process {
    ProcessType processType();
}
