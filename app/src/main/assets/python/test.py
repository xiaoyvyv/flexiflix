from bs4 import BeautifulSoup
import requests
import os

def main():
    url = "https://www.baidu.com"  # 替换为你要解析的网站 URL
    # print(os.environ["HOME"])
    print(__file__)
    try:
        # 发送 GET 请求并获取网页内容
        response = requests.get(url)
        if response.status_code == 200:
            # 使用 BeautifulSoup 解析 HTML
            soup = BeautifulSoup(response.text, 'html.parser')

            # 找到所有的 <a> 标签
            links = soup.find_all('a')

            # 打印所有链接的文本和链接地址
            for link in links:
                print("链接文本:", link.text)
                print("链接地址:", link['href'])
        else:
            print("获取网页失败，状态码:", response.status_code)
    except requests.exceptions.RequestException as e:
        print("获取网页失败:", e)

