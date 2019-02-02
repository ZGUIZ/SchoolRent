package com.example.amia.schoolrent.Task;

import com.example.amia.schoolrent.Bean.IdleInfo;

import java.util.List;

public interface IdleTask {
    List<IdleInfo> getListInfo(List<IdleInfo> list,int index);
}
