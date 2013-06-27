import java.security.MessageDigest
import sun.misc.BASE64Encoder
/**
 * Created with IntelliJ IDEA.
 * User: lenovo
 * Date: 13-6-25
 * Time: 下午2:40
 * To change this template use File | Settings | File Templates.
 */
class PasswordCodec {
    static encode = {str ->
        MessageDigest md = MessageDigest.getInstance('SHA')
        md.update(str.getBytes('UTF-8'))

        return (new BASE64Encoder()).encode(md.digest())


    }

}
