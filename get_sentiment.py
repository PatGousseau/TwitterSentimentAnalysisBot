import os
import sys
import pandas as pd
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

def sentiment_analyzer_scores(sentence):
    score = analyser.polarity_scores(sentence)
    return score['compound']

if __name__=='__main__':
    analyser = SentimentIntensityAnalyzer()
    out= {}

    with open('allTweets.txt', 'r') as f:
        tweets = f.readlines()

    for tweet in tweets:
        score = sentiment_analyzer_scores(tweet)
        out[tweet] = score

    with open('allTweets.txt', 'w') as f:
        for tweet in out.keys():
            f.write(str(out[tweet]) + '\n')
            f.write(tweet)

    
