import json
from abc import ABC
from typing import List, Dict
from ExtensionSource import Source
from ExtensionModel import (
    FlexMediaDetail,
    FlexMediaDetailTab,
    FlexMediaPlaylistUrl,
    FlexMediaSection,
    FlexMediaSectionItem,
    FlexMediaUser,
    FlexSearchOption,
)


# 创建 Source 的子类实例并调用方法
class MySource(Source, ABC):
    def fetch_home_sections(self) -> List[FlexMediaSection]:
        # 返回假数据
        sections = [
            FlexMediaSection(id="1", title="Section 1", items=[]),
            FlexMediaSection(id="2", title="Section 2", items=[]),
        ]
        return sections

    def fetch_section_media_pages(self, section_id: str, section_extras: Dict[str, str], page: int) -> \
            List[FlexMediaSectionItem]:
        pass

    def fetch_user_media_pages(self, user: FlexMediaUser) -> List[FlexMediaSectionItem]:
        pass

    def fetch_media_detail(self, media_id: str, extras: Dict[str, str]) -> FlexMediaDetail:
        pass

    def fetch_media_raw_url(self, playlist_url: FlexMediaPlaylistUrl) -> FlexMediaPlaylistUrl:
        pass

    def fetch_media_detail_relative(self, relative_tab: FlexMediaDetailTab, media_id: str, extras: Dict[str, str]) -> \
            List[FlexMediaSection]:
        pass

    def fetch_media_search_config(self) -> FlexSearchOption:
        pass

    def fetch_media_search_result(self, keyword: str, page: int, search_map: Dict[str, str]) -> \
            List[FlexMediaSectionItem]:
        pass

    def fetch_media_raw_url_default(self, playlist_url: FlexMediaPlaylistUrl) -> FlexMediaPlaylistUrl:
        pass


# 返回数据源
def defineSource() -> MySource:
    # 使用 MySource
    source = MySource()
    return source


if __name__ == '__main__':
    print('PyCharm')
    print(defineSource())
