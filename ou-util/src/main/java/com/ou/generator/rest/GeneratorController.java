package com.ou.generator.rest;

import com.ou.generator.domain.GeneratorSetting;
import com.ou.generator.service.AutoGeneratorService;
// import com.ou.generator.service.GeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *  代码生成
 * @author vince
 */
@Slf4j
@Controller
@RequestMapping("api/v1/util/generate")
public class GeneratorController {

   /* @Autowired
    private GeneratorService generatorService;*/

    @Autowired
    private AutoGeneratorService autoGeneratorService;

    @PostMapping("/code")
    // @PreAuthorize("hasAuthority('connection_list')")
    public ResponseEntity generateCode(HttpServletRequest request,
                                       @RequestBody(required = false) Map<String, Object> map) {
        /*String projectName = (String) map.get("projectName");
        List<String> tableIds = (List<String>) map.get("tableIds");
        List<Long> collect = tableIds.stream().map(Long::parseLong).collect(Collectors.toList());
        return ResponseEntity.ok(generatorService.generateCode(collect, projectName));*/
        GeneratorSetting settings = new GeneratorSetting();
        settings.setAuthor("ohYoung");
        settings.setPackageName("com.jxnx.structure");
        List<String> tables = new ArrayList<>();
        tables.add("structure_component");
        tables.add("structure_layout_data");
        tables.add("structure_layout_info");
        tables.add("structure_page");
        return ResponseEntity.ok(autoGeneratorService.generateCode(tables, settings));
    }

}
