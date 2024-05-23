import json
from abc import ABC, abstractmethod
from typing import List, Dict
from dataclasses import dataclass

# 导入定义的模型
from ExtensionModel import (
    serializable,
    FlexMediaDetail,
    FlexMediaDetailTab,
    FlexMediaPlaylistUrl,
    FlexMediaSection,
    FlexMediaSectionItem,
    FlexMediaUser,
    FlexSearchOption,
)


@dataclass
class Command:
    which: str
    extras: Dict[str, str]


class Source(ABC):
    """
    Source 接口
    """

    @staticmethod
    def serializable(data: object) -> str:
        return json.dumps(data, default=serializable)

    @abstractmethod
    def fetch_home_sections(self) -> List[FlexMediaSection]:
        """
        获取首页的分类栏位板块
        """
        pass

    @abstractmethod
    def fetch_section_media_pages(
            self, section_id: str, section_extras: Dict[str, str], page: int
    ) -> List[FlexMediaSectionItem]:
        """
        获取对应的某个 section 的全部分页数据
        """
        pass

    @abstractmethod
    def fetch_user_media_pages(self, user: FlexMediaUser) -> List[FlexMediaSectionItem]:
        """
        获取某个用户全部分页数据
        """
        pass

    @abstractmethod
    def fetch_media_detail(self, media_id: str, extras: Dict[str, str]) -> FlexMediaDetail:
        """
        获取媒体条目详情
        """
        pass

    @abstractmethod
    def fetch_media_raw_url(self, playlist_url: FlexMediaPlaylistUrl) -> FlexMediaPlaylistUrl:
        """
        获取媒体条目的原始播放链接
        """
        pass

    @abstractmethod
    def fetch_media_detail_relative(
            self, relative_tab: FlexMediaDetailTab, media_id: str, extras: Dict[str, str]
    ) -> List[FlexMediaSection]:
        """
        获取媒体条目详情，下方对应的推荐相关栏位板块
        """
        pass

    @abstractmethod
    def fetch_media_search_config(self) -> FlexSearchOption:
        """
        获取搜索的配置项数据，比如关键词的 key 和可选项等
        """
        pass

    @abstractmethod
    def fetch_media_search_result(
            self, keyword: str, page: int, search_map: Dict[str, str]
    ) -> List[FlexMediaSectionItem]:
        """
        搜索媒体数据
        """
        pass

    @abstractmethod
    def fetch_media_raw_url_default(self, playlist_url: FlexMediaPlaylistUrl) -> FlexMediaPlaylistUrl:
        """
        获取媒体条目的原始播放链接（默认实现）
        """
        pass
