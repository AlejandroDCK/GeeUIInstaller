package com.letianpai.robot.geeuilocaldownloader.parser

import java.io.Serializable

/**
 * @author liujunbin
 */
class DownloadFileInfo : Serializable {
    var url: String? = null
    var file_name: String? = null
    var downloadStatus: Int = 0
    var fileType: String? = null

    override fun toString(): String {
        return "DownloadFileInfo{" +
                "url='" + url + '\'' +
                ", file_name='" + file_name + '\'' +
                ", downloadStatus=" + downloadStatus +
                ", fileType='" + fileType + '\'' +
                '}'
    }
}
