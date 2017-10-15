package com.zwayam.appliespuller.be;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zwayam.appliespuller.common.AppConstants;
import com.zwayam.appliespuller.common.FileUtils;
import com.zwayam.appliespuller.common.HttpDownloadUtility;
import com.zwayam.appliespuller.common.JsonToObjMapper;
import com.zwayam.appliespuller.common.QueryExecuter;
import com.zwayam.appliespuller.common.RestWebServiceApiCall;
import com.zwayam.appliespuller.common.restResponse;
import com.zwayam.appliespuller.mongo.models.JobApplication;
import com.zwayam.appliespuller.mongo.repo.ApplicationTrackRepo;
import com.zwayam.appliespuller.mysql.models.AuthApiConfig;
import com.zwayam.appliespuller.mysql.models.PullAppliesApiConfig;
import com.zwayam.appliespuller.mysql.repo.AuthApiConfigRepo;
import com.zwayam.appliespuller.mysql.repo.PullAppliesApiConfigRepo;


@Component
public class PullAppliesComponent {
	static final Logger logger = Logger.getLogger(PullAppliesComponent.class);

	@Autowired
	PullAppliesApiConfigRepo apiConfigRepo;
	
	@Autowired
	ApplicationTrackRepo  applicationTrackRepo;
	
	@Autowired
	AuthApiConfigRepo authApiConfigRepo;
	
	
	
	@Scheduled(cron = "${appliesPullCron}")
	public void cronJobForPullingApplies() {
		logger.info("cronJobForPullingApplies...");
		List<PullAppliesApiConfig> pullAppliesApiConfig = (List<PullAppliesApiConfig>) apiConfigRepo
				.findAll();
		for (PullAppliesApiConfig configuration : pullAppliesApiConfig) {
			Calendar currentTime = Calendar.getInstance();
			Calendar nextExecutionTime = Calendar.getInstance();
			nextExecutionTime.setTime(configuration.nextTime);
			if (nextExecutionTime.compareTo(currentTime) < 0) { // check next
																// execution
																// time
				try {
					logger.info("Pulling Applies " + configuration.name + " starting...");

					nextExecutionTime.add(Calendar.MINUTE, Integer.valueOf(configuration.frequency));
					configuration.nextTime = nextExecutionTime.getTime();
					pullAppliesFromJobBoard(configuration);
					configuration.lastTime = currentTime.getTime();
					apiConfigRepo.save(configuration);
					logger.info("Pulling Applies " + configuration.name + "  has executed " + " Last executed :"
							+ configuration.lastTime + "  Next execution :" + configuration.nextTime);
				} catch (Exception e) {
					logger.error("Exception while executing cronJobForPullingApplies", e);

				}
			}

		}
	}
	public Object pullAppliesFromJobBoard(){
		
		return null;
	}
	public Object pullAppliesFromJobBoard(PullAppliesApiConfig apiConf){
		logger.info("pullAppliesFromJobBoard starting...");
		try {
			HashMap<String,String> accessKeys = accessTokenGenerator(apiConf);
			HashMap<String, String> urlParamsHM = jsonToMap(apiConf.urlParams);
			HashMap<String, String> jobAppMap=jsonToMap(apiConf.jobAppMapTemplate);
			List<HashMap<String, Object>> jobDatas = QueryExecuter.execute(apiConf.queries);
			logger.info("Jobs Found : "+jobDatas.size());
			for (HashMap<String, Object> row : jobDatas){
				row.forEach((key,val)->{
					if(urlParamsHM.containsKey(key)){
						urlParamsHM.put(key,String.valueOf(val));
					}});
				
				HashMap<String,String> headerParams = jsonToMap(apiConf.headerParams);
				headerParams = replaceVaraibles(headerParams,accessKeys);
				headerParams = replaceVaraibles(headerParams,jsonToMap(apiConf.authParams));
				String url=apiConf.apiBaseUrl;
				//String urlParams=apiConf.urlParams;
				if(url.contains("$boardId$"))
				{
					url=url.replace("$boardId$",(String) row.get("jobBoardId"));
				}
				if(url.contains("$sdate$"))
				{
					String sdate=row.get("sdate").toString();
					sdate=sdate.trim();
					sdate=URLEncoder.encode(sdate, "UTF-8");
					url=url.replace("$sdate$",sdate);
				}
				if(url.contains("$edate$"))
				{
					String edate = row.get("edate").toString();
					edate = URLEncoder.encode(edate, "UTF-8");
					url = url.replace("$edate$", edate);
				}
				System.out.println(url);
				//logger.info("current url: " +url );	
				restResponse response= RestWebServiceApiCall.executeWebServiceApi(url,apiConf.httpMethodType, headerParams, urlParamsHM, null);
				logger.info(response.result);
				System.out.println(response.result);
				//String response2="{    \"count\": 1,     \"results\": [        {            \"name\": \"YASH MALIK\",             \"email\": \"XXXXX@gmail.com\",             \"phone\": \"+91-XXXXXXXXXXX\",             \"company\": \"ABC Corporation\",             \"doc_id\": \"AGB0Iu2NoCZ0HgR7lrG3_-TLtukxnliRpyxi-GZ7mNOHAtQ39C_eB00HkPSwRHU_JM\",             \"last_active\": \"2017-03-01 20:15:23\",             \"last_modified\": \"2017-02-24 16:38:28\",             \"location\": \"Bangalore\",             \"total_work_experience\": \"13 Yrs 0 Month\",             \"experience_in_current_job\": \"0 Yr\",             \"functional_area\": \"Technical Support / Helpdesk\",             \"industry\": \"IT - Software\",             \"title\": \"manager technical/customer support\",             \"salary\": \"Rs. 20.00 Lacs\",             \"highest_edulevel\": \"Graduation\",             \"highest_edufield\": \"Other Management\",             \"highest_educollege\": \"Barah University\",             \"highest_edutype\": \"Full Time\",             \"dob\": \"1981-02-16\",             \"jobtitle\": \"manager technical/customer support\",             \"gender\": \"Male\",             \"skills\": [               \"operations management\",                \"backup\",                \"operations\"                       ],             \"job_detail\": [                    [                        {                        \"Years in the job\": \"0 Yr\",                         \"Functional Area\": \"Technical Support / Helpdesk\",                         \"Company\": \"ABC Corporation\",                         \"Industry Type\": \"IT - Software\",                         \"Job Title\": \"manager technical/customer support\"                        }                    ],                     [                        {                        \"Years in the job\": \"1 Yr\",                         \"Functional Area\": \"Operations Management / Process Analysis\",                         \"Company\": \"Adobe Systems India PVT LTD\",                         \"Industry Type\": \"IT - Software\",                         \"Job Title\": \"Customer Care manager\"                        },                         {                        \"Years in the job\": \"9 Yrs\",                         \"Functional Area\": \"Technical Support / Helpdesk\",                         \"Company\": \"Symantec software india pvt ltd.\",                         \"Industry Type\": \"IT - Software\",                         \"Job Title\": \"Enterprise Support Manager\"                        }                    ]            ],             \"education_detail\": [                    [                        {                        \"Institute Name\": \"Barah University\",                         \"Year Of Passing\": \"2003\",                         \"Qualification level\": \"Graduation\",                         \"Course Type\": \"Full Time\",                         \"Education Stream\": \"Other Management\"                        }                    ],                     [                        {                        \"Institute Name\": \"IN Bhopal\",                         \"Year Of Passing\": \"2002\",                         \"Qualification level\": \"Diploma / Vocational Course\",                         \"Course Type\": \"Full Time\",                         \"Education Stream\": \"Hospitality and Hotel Management\"                        },                         {                        \"Institute Name\": \"Barah University\",                         \"Year Of Passing\": \"2003\",                         \"Qualification level\": \"Graduation\",                         \"Course Type\": \"Correspondence\",                         \"Education Stream\": \"Commerce\"                        }                    ]           ],            \"preference\": [                        {                        \"Salary\": \"Rs 20 - 22 Lakh / Yr\",                         \"Shift Type\": \"Morning | Noon | Evening | Night | Split | Rotating\",                         \"Job Location\": \"Bangalore | Bangalore | Australia | Pune\",                         \"Job Type\": \"- Any -\",                         \"Functional Area\": \"Operations Management / Process Analysis | Customer Service (International) | Technical Support / Helpdesk\",                         \"Industry\": \"- Any - | IT - Software | BPO / Call Center | Internet / E-Commerce | IT - Hardware / Networking\"                        }           ],            \"preferred_locations\": [                        \"Bangalore\",                         \"All Australia\",                         \"Pune\"            ],             \"notice_period\": \"2 weeks\",             \"app_type\": \"n\",             \"app_date\": \"2017-02-24T16:50:17\",             \"resume\": \"http://recruiter.shine.com/api/v2/job/2813033/resume?candidate=5838bac33361\",             \"source\": \"Direct\",             \"unique_id\": \"67985f2b295575a62d7d35ccccxxxxx0709bae2e2106\"            } ]}";
				if (response.result != null) {
					logger.info("Applies : " + response.result.toString());
					JSONObject jsonObject = stringToJson(response.result.toString());
					// JSONObject jsonObject = stringToJson(response2);
					JSONArray array = (JSONArray) jsonObject.get("ApplicationList");
					if (array == null) {
						array = (JSONArray) jsonObject.get("results");
					}
					logger.info("Applies size: " + array.size());
					for (Object dataObj : array) {
						JSONObject data = (JSONObject) dataObj;
						HashMap<String, Object> hm = jsonToMap(data.toJSONString(), apiConf.name);
						JobApplication jobApplication;
						jobApplication = JsonToObjMapper.convertToJobApp(hm, jobAppMap);
						jobApplication.jobId = row.get("jobId").toString();
						jobApplication.companyId = row.get("companyId").toString();
						logger.error("jobId : "+jobApplication.jobId);
						logger.error("\ncompanyId : "+jobApplication.companyId);
						jobApplication.source = apiConf.name;

						if (jobApplication.resumeURL != null && !jobApplication.resumeURL.contains("https:")
								&& !jobApplication.resumeURL.contains("http:")) {
							jobApplication.resumeURL = jobApplication.resumeURL.replaceAll("\\s+", "");
							jobApplication.resumeURL = "https:" + jobApplication.resumeURL;
						}

						String resurl = jobApplication.resumeURL;
						logger.error(resurl);
						jobApplication.fileName = FileUtils.getFileNameFromURL(jobApplication.resumeURL, apiConf.name);
						logger.error(jobApplication.fileName);
						if (duplicateCheck(jobApplication)) {
							sendAppliesToCareerSite(jobApplication);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception pullAppliesFromJobBoard",e);
		} 
		return null;
	}

	private HashMap<String, String> replaceVaraibles(HashMap<String, String> hmWithOutData,
			HashMap<String, String> hmWithData) {
		if(hmWithOutData.size()>0&&hmWithData.size()>0){
		hmWithOutData.forEach((key,val)->{
				if(hmWithData.containsKey(val)){
					hmWithOutData.put(key,hmWithData.get(val));
				}});
		}
		return hmWithOutData;
	}

	private static JSONObject stringToJson(String jsonString) throws ParseException {
		JSONParser jsonParser = new  JSONParser();
		JSONObject jsonObject  = (JSONObject) jsonParser.parse(jsonString);
		return jsonObject;
	}

	private boolean duplicateCheck(JobApplication jobApplication) {
	 List<JobApplication> applicationTracks = applicationTrackRepo.findBySourceAndCompanyIdAndJobIdAndEmailId(jobApplication.source, jobApplication.companyId,jobApplication.jobId, jobApplication.emailId);
		if (applicationTracks.size() == 0) {
			saveJobAppTrack(jobApplication);
			return true;
		}
		return true;
	}

	private JobApplication saveJobAppTrack(JobApplication jobApplication) {
			jobApplication.date = new Date();
			jobApplication.applicantId ="";
			return applicationTrackRepo.save(jobApplication);
	}

	private  void sendAppliesToCareerSite(JobApplication jobApplication) throws MalformedURLException, IOException{
		try {
			
			String jobApplyuUrl = AppConstants.MAINDOMAIN+"ccubeAPI/applyJob?source="+jobApplication.source;
			logger.info("Job App : "+jobApplication.firstName +" "+jobApplication.companyId +" "+jobApplication.jobId
					+" "+jobApplication.resumeURL + " "+jobApplyuUrl);
			//jobApplication.resumeURL=null;  // resumeurl cannot be used to download the resume
			InputStream inputStream=null;
			Boolean resumePresent=true;
			try {
				if(jobApplication.source.equals("Shine"))
				{
					PullAppliesApiConfig apiConfig=apiConfigRepo.findByName("shine");
					HashMap<String,String> accessKeys = accessTokenGenerator(apiConfig);
					HashMap<String,String> headerParams = jsonToMap(apiConfig.headerParams);
					headerParams = replaceVaraibles(headerParams,accessKeys);
					headerParams = replaceVaraibles(headerParams,jsonToMap(apiConfig.authParams));
					restResponse response3= RestWebServiceApiCall.executeWebServiceApi(jobApplication.resumeURL,apiConfig.httpMethodType, headerParams, null, null);
					System.out.println(response3.result.toString());
				}
				else {
				if(jobApplication.resumeURL !=null && !jobApplication.resumeURL.isEmpty()){
					inputStream = new URL(jobApplication.resumeURL).openStream();
				 }
				}
			} catch (Exception e) {
				resumePresent=false;
				logger.error("Error on getting stream of resume",e);
			}
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			if(resumePresent){
				builder.addBinaryBody("file",inputStream, ContentType.APPLICATION_OCTET_STREAM,jobApplication.fileName);
			}
			builder.addTextBody("data",new ObjectMapper().writeValueAsString(jobApplication) )
			.addTextBody("siteUrl",jobApplyuUrl)
			.addTextBody("externalSource",jobApplication.source)
			.addTextBody("replaceApplication", "yes")
			.addTextBody("jobReferrerEmailId", "null")
			.addTextBody("siteKey", "<NO_SITEKEY>");
			restResponse response =RestWebServiceApiCall.httpPostAPI(jobApplyuUrl, builder);
			logger.error("executing send applies to cariersite");
			HashMap<String,String> res = new ObjectMapper().readValue(response.result.toString(),new TypeReference<HashMap<String, String>>(){});
			logger.error("response\n"+response.result.toString());
			if(res.get("code").equals("200")){
				List<JobApplication> applicationTracks = applicationTrackRepo.findBySourceAndCompanyIdAndJobIdAndEmailId(jobApplication.source, jobApplication.companyId,jobApplication.jobId, jobApplication.emailId);
				if(applicationTracks.size()>0){
					JobApplication applicationTrack = applicationTracks.get(0);
					applicationTrack.applicantId=res.get("jobApplyId");
					applicationTrackRepo.save(applicationTrack);
					logger.info("Apply saved : " +applicationTrack.applicantId );
				}
			}else{
				logger.info("Job App Response: "+jobApplication.firstName +" "+jobApplication.companyId +" "+jobApplication.jobId
						+" "+jobApplication.resumeURL + " "+jobApplyuUrl); 
				logger.info("Apply response : " + new ObjectMapper().writeValueAsString(res)); 
			}
		} catch (Exception e) {
			logger.error("Exception sendAppliesToCareerSite",e);
		}
	}

	private HashMap<String,String> jsonToMap(String json) throws JsonParseException, JsonMappingException, IOException{
		
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
			return mapper.readValue(json, typeRef);
				
	}
	private static HashMap<String,Object> jsonToMap(String data,String name) throws JsonParseException, JsonMappingException, IOException, ParseException{
		HashMap<String, Object> hm=new HashMap<>();
		JSONObject json = stringToJson(data);
		if(json != null) {
	        hm = toMap(json);
	    }		
		return hm;
		
}
	public static HashMap<String, Object> toMap(JSONObject object) {
	    HashMap<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = object.keySet().iterator();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);

	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

	public static List<Object> toList(JSONArray array){
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i <array.size(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}
	public HashMap<String, String> accessTokenGenerator(PullAppliesApiConfig apiConf){
		AuthApiConfig authApiConfig = authApiConfigRepo.findByName(apiConf.name);
		HashMap<String,String> hm=new HashMap<>();
		try {
			if(authApiConfig!=null){
			HashMap<String, String> authKeys = jsonToMap(apiConf.authParams);
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
			String todaysDate = format.format(cal.getTime());
			String data = authKeys.get("email") + authKeys.get("Appid") + todaysDate;
			SecretKeySpec signingKey = new SecretKeySpec(authKeys.get("Secret").getBytes(), "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);
			String signature = toHexString(mac.doFinal(data.getBytes()));
			JSONObject json = new JSONObject();
			json.put("signature", signature);
			json.put("appId", authKeys.get("Appid"));
			restResponse response = RestWebServiceApiCall.executeWebServiceApi(authApiConfig.apiBaseUrl,
					authApiConfig.httpMethodType, jsonToMap(authApiConfig.headerParams), null, json.toJSONString());
			hm =  jsonToMap(response.result.toString());
			
			signingKey = new SecretKeySpec(hm.get("token").getBytes(), "HmacSHA1");
            mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            data = authKeys.get("Appid")+authKeys.get("email")+todaysDate;
            signature = toHexString(mac.doFinal(data.getBytes()));
            hm.put("token", signature);
           	logger.info("Token "+new ObjectMapper().writeValueAsString(hm));
			}
			return hm;
		} catch (Exception e) {
			logger.error("Error while generating the access token " + apiConf.name, e);
			return null;
		}
	}
	 
	    private static String toHexString(byte[] bytes) {
	        Formatter formatter = new Formatter();
	        for (byte b : bytes) {
	            formatter.format("%02x", b);
	        }
	        return formatter.toString();
	    }

	    
	  
	    
}
