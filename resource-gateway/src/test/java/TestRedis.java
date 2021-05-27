import com.auth.gw.AuthGatewayApplication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthGatewayApplication.class)
public class TestRedis {
    @Autowired
    @Qualifier("stringBytesRedisTemplate")
    private RedisTemplate<String, byte[]> redisTemplate;

    @Test
    public void test() {
        byte[] obj = redisTemplate.opsForValue().get("access:4Ywm47t7WdhzndMkUugIEM6K5lM");

        System.out.println(obj);
    }

    @Test
    public void ParseJwt() {

        SecretKey secretKey = new SecretKeySpec("ASDFASFsdfsdfsdfsfadsf234asdfasfdas".getBytes(), SignatureAlgorithm.HS256.getJcaName());
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdE9hdXRoIl0sInVzZXJfbmFtZSI6ImFkbWluIiwiZXhwIjoxNjIyMDk4Mjc3LCJ1c2VyTmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOm51bGwsImp0aSI6InBOb3lMamZNRTRqaDU4MXBuSGdsTWktV1RoWSIsImNsaWVudF9pZCI6ImNsaWVudCJ9.7z1Gqmz9J8r8REAcHOXs-ZtokOJtDQ9XZ8-8RlqZf30";
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
        //解析自定义的claim中的内容
        String companyId = (String) claims.get("user_name");
        List<String> companyName = (List<String>) claims.get("aud");
        System.out.println(companyId);
        System.out.println(companyName);
    }
}
