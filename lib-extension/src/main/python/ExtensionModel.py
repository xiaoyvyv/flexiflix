from dataclasses import dataclass, field
from typing import Optional, List, Dict

UNKNOWN_STRING = ""


def serializable(obj: object) -> object:
    if isinstance(obj, MediaSourceInfo):
        return obj.__dict__
    elif isinstance(obj, FlexKeyValue):
        return obj.__dict__
    elif isinstance(obj, FlexMediaTag):
        return obj.__dict__
    elif isinstance(obj, FlexMediaUser):
        return obj.__dict__
    elif isinstance(obj, ImageLayout):
        return obj.__dict__
    elif isinstance(obj, OverlayText):
        return obj.__dict__
    elif isinstance(obj, FlexMediaSectionItem):
        return obj.__dict__
    elif isinstance(obj, FlexMediaSection):
        return obj.__dict__
    elif isinstance(obj, FlexMediaDetailSeries):
        return obj.__dict__
    elif isinstance(obj, FlexMediaDetailTab):
        return obj.__dict__
    elif isinstance(obj, SourceUrl):
        return obj.__dict__
    elif isinstance(obj, FlexMediaPlaylistUrl):
        return obj.__dict__
    elif isinstance(obj, FlexMediaPlaylist):
        return obj.__dict__
    elif isinstance(obj, FlexMediaDetail):
        return obj.__dict__
    elif isinstance(obj, FlexSearchOptionItem):
        return obj.__dict__
    elif isinstance(obj, FlexSearchOption):
        return obj.__dict__
    else:
        raise TypeError(f"Object of type {obj.__class__.__name__} is not JSON serializable")


@dataclass
class MediaSourceInfo:
    """
    数据源信息类

    Args:
        id (str): 数据源唯一ID
        name (str): 数据源名称
        description (str): 数据源描述
        icon (str): 数据源图标
        author (str): 数据源作者
        nsfw (bool): 是否包含成人内容
        versionCode (int): 数据源版本代码
        versionName (str): 数据源版本名称
    """
    id: str
    name: str
    description: str
    icon: str
    author: str
    nsfw: bool
    versionCode: int
    versionName: str


@dataclass
class FlexKeyValue:
    key: str
    value: str


@dataclass
class FlexMediaTag:
    id: str
    name: str


@dataclass
class FlexMediaUser:
    id: str = UNKNOWN_STRING
    name: str = UNKNOWN_STRING
    avatar: str = UNKNOWN_STRING
    role: Optional[str] = UNKNOWN_STRING


@dataclass
class ImageLayout:
    widthDp: int = 140
    aspectRatio: float = 16 / 9


@dataclass
class OverlayText:
    topStart: str = ""
    topEnd: str = ""
    bottomStart: str = ""
    bottomEnd: str = ""


@dataclass
class FlexMediaSectionItem:
    id: str
    title: str
    cover: str
    description: Optional[str] = None
    user: Optional[FlexMediaUser] = None
    extras: Dict[str, str] = field(default_factory=dict)
    overlay: Optional[OverlayText] = None
    layout: Optional[ImageLayout] = None


@dataclass
class FlexMediaSection:
    id: str
    title: str
    items: List[FlexMediaSectionItem]
    extras: Optional[Dict[str, str]] = field(default_factory=dict)


@dataclass
class FlexMediaDetailSeries:
    title: str
    mediaId: str
    count: int = 0
    items: List[FlexMediaSectionItem] = field(default_factory=list)


@dataclass
class FlexMediaDetailTab:
    id: str
    mediaId: str
    title: str
    extras: Optional[Dict[str, str]] = field(default_factory=dict)


@dataclass
class SourceUrl:
    name: str
    rawUrl: str
    size: Optional[str] = None
    type: Optional[str] = None


@dataclass
class FlexMediaPlaylistUrl:
    id: str
    title: str
    mediaUrls: List[SourceUrl] = field(default_factory=list)
    cover: Optional[str] = ""


@dataclass
class FlexMediaPlaylist:
    title: str = "默认"
    items: List[FlexMediaPlaylistUrl] = field(default_factory=list)


@dataclass
class FlexMediaDetail:
    id: str
    title: str
    description: str
    cover: str
    url: str
    type: Optional[str] = UNKNOWN_STRING
    playCount: Optional[str] = UNKNOWN_STRING
    createAt: Optional[str] = UNKNOWN_STRING
    duration: int = 0
    size: Optional[str] = UNKNOWN_STRING
    publisher: Optional[FlexMediaUser] = field(default_factory=FlexMediaUser)
    playlist: List[FlexMediaPlaylist] = field(default_factory=list)
    series: List[FlexMediaDetailSeries] = field(default_factory=list)
    tags: List[FlexMediaTag] = field(default_factory=list)
    relativeTabs: List[FlexMediaDetailTab] = field(default_factory=list)
    extras: Dict[str, str] = field(default_factory=dict)


@dataclass
class FlexSearchOptionItem:
    key: str
    keyLabel: Optional[str] = None
    values: Optional[List[FlexKeyValue]] = None
    maxSelect: int = 1
    mergeSymbol: Optional[str] = None


@dataclass
class FlexSearchOption:
    keywordKey: str
    options: Optional[List[FlexSearchOptionItem]] = None
