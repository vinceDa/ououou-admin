
package ${package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ${author}
 * @date ${date}
 */

@SpringBootApplication(scanBasePackages = "${package}.*")
public class ${project?cap_first}Application {

    public static void main(String[] args) {
        SpringApplication.run(${project?cap_first}Application.class, args);
    }

}

