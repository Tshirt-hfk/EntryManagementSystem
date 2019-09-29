package com.entry.controller;

import com.alibaba.fastjson.JSONObject;
import com.entry.dto.BaseResultFactory;
import com.entry.entity.mysql.*;
import com.entry.repository.mysql.*;
import com.entry.service.HttpRequestService;
import com.entry.service.SubjectManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class AdministratorController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupMemberRepository groupMemberRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    SubjectManagementService subjectManagementService;

    @Autowired
    HttpRequestService httpRequestService;

    /**
     * 管理员获取所有待审核词条
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/admin/getRecord")
    @CrossOrigin
    public ResponseEntity<?> getRecord(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            //得到待审核记录
            List<Record> records = recordRepository.findAllByState(Record.TOAUDITED);
            List<Object> list = new ArrayList<>();
            HashMap<String, Object> tmp = null;
            for(Record record: records){
                tmp = new HashMap<>();
                tmp.put("id", record.getId());
                tmp.put("content", record.getContent());
                tmp.put("saveTime", record.getSaveTime());
                //tmp.put("judgeTime", record.getJudgeTime());
                tmp.put("reason", record.getAdmitReason());
                tmp.put("modifyReason", record.getModifyReason());
                tmp.put("name", record.getEntryName());
                tmp.put("field", record.getField());
                tmp.put("isTask", "false");
                list.add(tmp);
            }
            HashMap<String, Object> result = new HashMap<>();
            result.put("records", list);
            return new ResponseEntity<>(BaseResultFactory.build(result, "success"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"错误"),HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 管理员审核词条
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/admin/auditRecord")
    @CrossOrigin
    public ResponseEntity<?> auditRecord(HttpServletRequest request, @RequestBody String jsonParam){
        try{
            JSONObject form = JSONObject.parseObject(jsonParam);
            Boolean pass = (Boolean)form.get("pass");
            Integer recordId = (Integer)form.get("recordId");
            String reason = (String) form.get("reason");
            Record record = recordRepository.findRecordById(recordId);
            if(pass){
                record.setState(Record.PASS);
                return new ResponseEntity<>(BaseResultFactory.build("审核通过"), HttpStatus.OK);
            }else{
                record.setState(Record.UNPASS);
                record.setAdmitReason(reason);
                return new ResponseEntity<>(BaseResultFactory.build("审核不通过"), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 管理员获取所有权限申请
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/admin/getApplication")
    @CrossOrigin
    public ResponseEntity<?> getApplication(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            //得到待审核权限申请
            JSONObject form = JSONObject.parseObject(jsonParam);
            Integer affair = form.getInteger("affair");
            List<Application> applications = applicationRepository.findAllByAffairAndState(affair, Application.UNADUIT);
            List<Object> list = new ArrayList<>();
            HashMap<String, Object> tmp = null;
            for(Application application: applications){
                tmp = new HashMap<>();
                User user = application.getUser();
                tmp.put("id", application.getId());
                tmp.put("applicantID", user.getName());
                tmp.put("submitVersion", user.getSubmitVersion());
                tmp.put("passVersion", user.getPassVersion());
                tmp.put("premiumVersion", user.getPremiumVersion());
                list.add(tmp);
            }
            HashMap<String, Object> result = new HashMap<>();
            result.put("applications", list);
            return new ResponseEntity<>(BaseResultFactory.build(result, "success"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 管理员审核权限
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/admin/auditApplication")
    @CrossOrigin
    public ResponseEntity<?> auditApplication(HttpServletRequest request, @RequestBody String jsonParam){
        try{
            JSONObject form = JSONObject.parseObject(jsonParam);
            Boolean pass = (Boolean)form.get("pass");
            Integer applicationId = (Integer)form.get("applicationId");
            String reason = (String) form.get("reason");
            Application application = applicationRepository.findApplicationById(applicationId);
            // TODO 向用户发送一个消息提醒
            if(pass){
//                User user = application.getUser();
//                user.setAuthorith(User.SUBJECTMAKER);
                application.setState(Application.PASS);
                applicationRepository.save(application);
                return new ResponseEntity<>(BaseResultFactory.build("申请成功"), HttpStatus.OK);
            }else{
                application.setState(Application.UNPASS);
                applicationRepository.save(application);
                return new ResponseEntity<>(BaseResultFactory.build("申请失败"), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }
}
