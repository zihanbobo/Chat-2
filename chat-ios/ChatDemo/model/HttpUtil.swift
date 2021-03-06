//
//  HttpUtil.swift
//  ChatDemo
//
//  Created by zzy on 2019/2/14.
//  Copyright © 2019 pwc. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

protocol HttpRequestCallback {
    func onSuccess(data:JSON)
    func onFail(code:Int,message:String)
}

class HttpBaseModel{
    // 基本配置
    static let sharedSessionManager: AlamofireExtension<URLSessionConfiguration> = {
        let configuration = URLSessionConfiguration.default
        configuration.timeoutIntervalForRequest = 5
        return AlamofireExtension(configuration)
    }()
    
    var delegate:HttpRequestCallback?

    private func request(httpMethod method:HTTPMethod,url urlString:String,
                         requestParam requestData:Dictionary<String,Any>,
                         logTip logText:String){
        // 添加默认参数（公有body部分等）
        // let params:Dictionary<String,Any> = addBaseParams(requestData)
        // 输出提交参数
        print("\(logText)-->API:\(urlString)")
        print("\(logText)-->提交参数：\(requestData)")
        let encode:ParameterEncoding = method == .post ? JSONEncoding.default: URLEncoding.default
        AF.request(urlString,method: method,parameters:requestData,encoding:encode).response { (result) in
            guard result.response != nil else {
                let error = result.error?.localizedDescription ?? ""
                print("错误原因：\(error)")
                self.delegate?.onFail(code: -1, message: "出错：\(error)")
                return
            }
            
            if let data = result.data, let utf8Text = String(data: data, encoding: .utf8) {
                print("\(logText)-->返回结果： \(utf8Text)")
                if utf8Text.count==0{
                    self.delegate?.onFail(code: -1, message: "返回数据为空")
                    return
                }
                
                let json = JSON(parseJSON:utf8Text)
                if(json["code"].int == 200){
                    self.delegate?.onSuccess(data: json["data"])
                }else{
                    var error:String = "解析错误"
                    if let errorMsg = json["msg"].string{
                        error = errorMsg
                    }
                    self.delegate?.onFail(code: json["code"].int!, message: error)
                }
            }else{
                print("\(logText)-->出错： \(String(describing: result.error?.localizedDescription))")
                self.delegate?.onFail(code: -1, message: "\(logText)出错")
            }
        }
    }
    
    private func addBaseParams(_ requestData:Dictionary<String,Any>)->Dictionary<String,Any> {
        // 添加默认参数
        let userDefaults:UserDefaults = UserDefaults.standard
        let baseData = JSON(parseJSON:userDefaults.string(forKey: "data")!)
        var params = requestData
        params.updateValue(baseData["AccountId"].int!, forKey: "AccountId")
        params.updateValue(baseData["AccountName"].string!, forKey: "AccountName")
        params.updateValue(baseData["UserType"].int!, forKey: "UserType")
        params.updateValue(baseData["OrgNo"].string!, forKey: "OrgNo")
        params.updateValue(baseData["OrgName"].string!, forKey: "OrgName")
        params.updateValue(baseData["Token"].string!, forKey: "Token")
        params.updateValue("zh-cn", forKey: "Language")
        return params
    }
    
    func doGet(_ url:String,_ requestData:Dictionary<String,Any>,_ logText:String){
        request(httpMethod: .get, url: url, requestParam: requestData, logTip: logText)
    }
    
    func doPost(_ url:String,_ requestData:Dictionary<String,Any>,_ logText:String){
        request(httpMethod: .post, url: url, requestParam: requestData, logTip: logText)
    }
}

class HttpBlockModel:HttpBaseModel,HttpRequestCallback{
    var success:((_ data:JSON)->Void)?
    var fail:((_ code:Int,_ message:String)->Void)?
    
    // @escaping提醒用户注意循环引用
    init(success successCallback:@escaping (_ data:JSON)->Void,fail failCallback:@escaping (_ code:Int,_ message:String)->Void) {
        super.init()
        
        self.success = successCallback
        self.fail = failCallback
        self.delegate = self
    }
    
    func onSuccess(data: JSON) {
        self.success!(data)
    }
    
    func onFail(code: Int, message: String) {
        self.fail!(code,message)
    }
}


