//
//  FriendListViewController.swift
//  ChatDemo
//
//  Created by zzy on 2020/7/7.
//  Copyright © 2020 pwc. All rights reserved.
//

import UIKit

class FriendListViewController: BaseUIViewController,UITableViewDataSource,UITableViewDelegate{
    @IBOutlet weak var tableView: UITableView!

    private var chatClient = ChatClient.shared
    private var chatUserManager = ChatUserManager.shared
    private var toUser:User!
    private var groupList:Array<Group> = []
    private var friendList:Array<Friend> = []
    
    deinit {
        chatUserManager.removeObserver(self, forKeyPath: "userList")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        tableView.dataSource = self
        tableView.delegate = self
        tableView.tableFooterView = UIView.init(frame: CGRect.zero)
        
        // 初始化
        groupList = Array()
        friendList = Array()
        
        // kvo
        chatUserManager.addObserver(self, forKeyPath: "userList", options: .new, context: nil)
        // 获取好友列表
        getFriendList()
        getGroupList()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationItem.hidesBackButton = true
        self.navigationItem.title = chatClient.connectServerSuccess ? "已连接" : "未连接"
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "jump"){
            // 跳转传值
            let controller:ChatViewController = segue.destination as! ChatViewController
            controller.chatUser = toUser
        }
    }
    
    private func getFriendList(){
        showLoadingDialog(tipMessage: "正在加载数据...")
        let request:HttpBlockModel = HttpBlockModel(success: {data in
            print("接口返回好友列表：\(data)")
            self.hideLoadingDialog()
            let dic = data.dictionary
            let list = dic!["list"]
            let rawData = try! list?.rawData()
            let friends:Array<Friend> = JsonUtil.jsonData2Model(rawData!, Array<Friend>.self)
            if(friends.count <= 0){
                return
            }
            print("好友列表长度1：\(friends.count)")
            self.friendList.append(contentsOf: friends)
            print("好友列表长度2：\(self.friendList.count)")
            self.tableView.reloadData()
        }, fail: {(code,msg) in
            self.hideLoadingDialog()
            self.showToast("加载数据出错：\(msg)")
        })
        var requestData:Dictionary<String,Any> = [:]
        requestData["userId"] = CacheManager.shared.getUserId()
        request.doGet(ApiConfig.getFriendList, requestData, "加载数据")
    }
    
    private func getGroupList(){
        showLoadingDialog(tipMessage: "正在加载数据...")
        let request:HttpBlockModel = HttpBlockModel(success: {data in
            print("好友列表：\(data)")
            self.hideLoadingDialog()
            let dic = data.dictionary
            let list = dic!["list"]
            let rawData = try! list?.rawData()
            let groups:Array<Group> = JsonUtil.jsonData2Model(rawData!, Array<Group>.self)
            if(groups.count <= 0){
                return
            }
            print("群组列表长度1：\(groups.count)")
            self.groupList.append(contentsOf: groups)
            print("群组列表长度2：\(self.groupList.count)")
            self.tableView.reloadData()
        }, fail: {(code,msg) in
            self.hideLoadingDialog()
            self.showToast("加载数据出错：\(msg)")
        })
        var requestData:Dictionary<String,Any> = [:]
        requestData["userId"] = CacheManager.shared.getUserId()
        request.doGet(ApiConfig.getGroupList, requestData, "加载数据")
    }
    
    /**tableView相关**/
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String?{
        return section == 0 ? "群组列表" : "好友列表"
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat{
        return 50
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let count = section == 0 ? groupList.count: friendList.count
        print("数目为：\(count)")
        return count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell",for: indexPath)
        
        if(indexPath.section == 1){
            cell.textLabel?.text = friendList[indexPath.row].name
        }else{
            cell.textLabel?.text = groupList[indexPath.row].name
        }
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        // 取消选中
        tableView.deselectRow(at: indexPath, animated: true)
        // 打开聊天页面
//        toUser = chatUserManager.userList[indexPath.row]
//        self.navigationItem.title = "返回"
//        self.performSegue(withIdentifier: "jump", sender: self)
    }
        
}