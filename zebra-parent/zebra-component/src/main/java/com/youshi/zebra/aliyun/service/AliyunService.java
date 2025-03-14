package com.youshi.zebra.aliyun.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.ram.model.v20150501.GetPolicyRequest;
import com.aliyuncs.ram.model.v20150501.GetPolicyResponse;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse.Credentials;
import com.dorado.framework.tuple.TwoTuple;
import com.youshi.zebra.aliyun.constants.AliyunConstants;
import com.youshi.zebra.exception.base.DoradoRuntimeException;

/**
 * 
 * @author wangsch
 * @date 2017年3月24日
 */
@Service
public class AliyunService {
	
	private static final Logger logger = LoggerFactory.getLogger(AliyunService.class);
	
	public static final String STS_REGION = "cn-hangzhou";
	
	public static final String RAM_REGION = "cn-hangzhou";
	
	private static final int MAX_TRY_COUNT = 5;

	private AssumeRoleResponse getStsAssumeRole(String accessKeyId, String accessKeySecret,
			String roleArn, String roleSessionName, String policy, long expireSeconds) 
					throws ServerException, ClientException {
		IClientProfile profile = DefaultProfile.getProfile(STS_REGION, accessKeyId, accessKeySecret);
		DefaultAcsClient client = new DefaultAcsClient(profile);
		final AssumeRoleRequest request = new AssumeRoleRequest();
		request.setRoleArn(roleArn);
		request.setRoleSessionName(roleSessionName);
		request.setPolicy(policy);
		request.setDurationSeconds(expireSeconds);
		
		final AssumeRoleResponse response = client.getAcsResponse(request);
		return response;
	}
	
	private AssumeRoleResponse getStsAssumeRole(
			String roleArn, String roleSessionName, String policy, long expireSeconds) 
			throws ServerException, ClientException {
		return getStsAssumeRole(AliyunConstants.ACCESS_KEY_ID, AliyunConstants.ACCESS_KEY_SECRET, 
				roleArn, roleSessionName, policy, expireSeconds);
	}
	
	public TwoTuple<String, String> getStsAccount(
			String roleArn, String roleSessionName, String policy, long expireSeconds) {
		AssumeRoleResponse resp = null;
		int tryCount = 0;
		while(++tryCount <= MAX_TRY_COUNT) {
			try {
				resp = getStsAssumeRole(roleArn, roleSessionName, policy, expireSeconds);
				break;
			} catch (ClientException e) {
				logger.error("Ops, Fail call aliyun sts api, "
						+ "requestId: {}, errorCode: {}, errorMsg: {}, errorType: {}. already try {} times...", 
						e.getRequestId(), e.getErrCode(), e.getErrMsg(), e.getErrorType(), tryCount);
				logger.error("", e);
			}
		}
		
		if(resp == null) {
			logger.error("Fail call aliyun sts api {} times", tryCount);
			throw new DoradoRuntimeException();
		}
		
		Credentials credentials = resp.getCredentials();
		return new TwoTuple<String, String>(credentials.getAccessKeyId(), credentials.getAccessKeySecret());
	}
	
	// FIXME NoPermission : You are not authorized to do this action. Resource: acs:ram:*:1887933298995758:policy/AliyunOSSZebraVideoDevUpload Action: ram:GetPolicy
	public GetPolicyResponse getPolicy(String accessKeyId, String accessKeySecret, String policyType, String policyName) 
			throws ServerException, ClientException {
		IClientProfile profile = DefaultProfile.getProfile(RAM_REGION, accessKeyId, accessKeySecret);
		DefaultAcsClient client = new DefaultAcsClient(profile);
		GetPolicyRequest request = new GetPolicyRequest();
		request.setPolicyType(policyType);
		request.setPolicyName(policyName);
		GetPolicyResponse resp = client.getAcsResponse(request);
		
		return resp;
	}
	
	public GetPolicyResponse getPolicy(String policyType, String policyName) 
			throws ServerException, ClientException {
		return getPolicy(AliyunConstants.ACCESS_KEY_ID, AliyunConstants.ACCESS_KEY_SECRET, policyType, policyName);
	}
	
	public String getRamPolicy(String policyType, String policyName) {
		GetPolicyResponse resp = null;
		int tryCount = 0;
		while(++tryCount < MAX_TRY_COUNT) {
			try {
				resp = getPolicy(policyType, policyName);
				break;
			} catch (ClientException e) {
				logger.error("Ops, Fail call aliyun ram api, "
						+ "requestId: {}, errorCode: {}, errorMsg: {}, errorType: {}. already try {} times...", 
						e.getRequestId(), e.getErrCode(), e.getErrMsg(), e.getErrorType(), tryCount);
				logger.error("", e);
			}
		}
		
		if(resp == null) {
			logger.error("Fail call aliyun ram api {} times", tryCount);
			throw new DoradoRuntimeException();
		}
		
		String json = resp.getPolicy().getPolicyDocument();
		return json;
	}
}
