import sys
# tweepyをインストールしたディレクトリ相対パスを指定する
sys.path.append('tweepy')
# coding: UTF-8
import json
import tweepy
import random

import boto3

# バケット名,オブジェクト名
BUCKET_NAME = 'lycoris-recoil-bot'
OBJECT_KEY_NAME = 'speech.json'

TWEET_HASHTAG='#リコリコ #リコリス・リコイル #リコリス #lycorisrecoil #bot'

s3 = boto3.resource('s3')

def lambda_handler(event, context):
    bucket = s3.Bucket(BUCKET_NAME)
    obj = bucket.Object(OBJECT_KEY_NAME)

    response = obj.get()    
    body = response['Body'].read()
    speeches = json.loads(body.decode('utf-8'))

    index = random.randint(0, len(speeches['speeches'])-1)
    tweet_text = create_tweet_text(speeches['speeches'][index])

    # print(tweet_text)

    # 設定ファイル読み込み
    config = json.load(open('config.json', 'r'))
    # Twitterオブジェクト設定
    client = tweepy.Client(config['twitter']['bearerToken'], config['twitter']['consumerKey'],
                           config['twitter']['consumerSecret'], config['twitter']['accessToken'], config['twitter']['accessTokenSecret'])
    # ツイート
    client.create_tweet(text=tweet_text)

def create_tweet_text(json_value):
    return json_value['words'] + '\n\n' + json_value['scene'] + ' #' + json_value['actor'] + '\n' + TWEET_HASHTAG
