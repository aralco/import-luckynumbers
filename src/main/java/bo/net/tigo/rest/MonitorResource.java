package bo.net.tigo.rest;

import bo.net.tigo.model.Job;
import bo.net.tigo.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by aralco on 10/29/14.
 */
@Controller
@RequestMapping(value = "/monitor",
        produces = "application/json")
public class MonitorResource {

    @Autowired
    private MonitorService monitorService;
    private static final Logger logger = LoggerFactory.getLogger(MonitorResource.class);

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<Job>> monitorJobs(
            @RequestParam(value = "jobId", defaultValue = "0") Long jobId,
            @RequestParam(value = "owner", defaultValue = "NA") String owner,
            @RequestParam(value = "jobState", defaultValue = "NA") String jobState,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to)   {
        logger.info("monitorJobs:jobId="+jobId+", owner="+owner+", jobState="+jobState+", from="+from+", to="+to);
        List<Job> jobs = monitorService.monitorJobs(jobId, owner, jobState, from, to);
        return new ResponseEntity<List<Job>>(jobs, HttpStatus.OK);
    }

    @RequestMapping(value = "/audit/in/{taskId}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<String>> getInFile(@PathVariable Long taskId)   {
        logger.info("lookup InAudit for taskId:"+taskId);
        List<String> rows = monitorService.getInFile(taskId);
        logger.info("inFile: "+rows);
        return new ResponseEntity<List<String>>(rows, HttpStatus.OK);
    }

    @RequestMapping(value = "/audit/out/{taskId}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<String>> getOutFile(@PathVariable Long taskId)   {
        logger.info("lookup OutAudit for taskId:"+taskId);
        List<String> rows = monitorService.getOutFile(taskId);
        logger.info("outFile: "+rows);
        return new ResponseEntity<List<String>>(rows, HttpStatus.OK);
    }


}
