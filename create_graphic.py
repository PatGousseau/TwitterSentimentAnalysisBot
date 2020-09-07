import matplotlib.pyplot as plt
import sys
import numpy as np
import seaborn as sns
#import plotly.express as px
import pandas as pd
plt.style.use('seaborn')
plt.rcParams.update({'text.color' : "#363636",
                     'axes.labelcolor' : "#363636",
                     'font.family': 'Open Sans'})


def bend(w, s):
    '''
    Clean line break with at most 'w' characters per line
    '''
    s = s.split(" ")
    lst = filter(None, s)
    new_lst = [""]
    i = 0
    for word in lst:
        line = new_lst[i] + " " + word
        if(new_lst[i] == ""):
            line = word
        if(len(word)  > w):
            while(len(word)  > w):
                new_lst.append(word[:w])
                i += 1
                word = word[w:]
            i += 1
            new_lst.append(word)
        elif(len(line) > w):
           new_lst.append(word)
           i += 1        
        else:
            new_lst[i] = line
    return "\n".join(new_lst)


user = sys.argv[1] # User's handle

with open('allTweets.txt', 'r') as f:
    lines = f.readlines()
    tweets = lines[1::2]
    scores = np.array(lines[::2]).astype(float)

    # Redistribute
    #scores = [(score/abs(score)) * (abs(score)**(1/2)) if score!=0 else 0 for score in scores]
    n = len(np.where(scores==0)[0])
    scores = scores[np.where(scores!=0)]
    scores = np.append(scores, np.random.uniform(-0.25, 0.25, n))
    

with open('tweetExamples.txt', 'r') as f:
    ex_tweets = f.readlines()
    pos = ex_tweets[0]
    neg = ex_tweets[1]

#TEST ----------------
    
#N=400
#mu, sigma = -0.6, 0.6
#mu2, sigma2 = 0.46, 0.3
#X1 = np.random.normal(mu, sigma, N)
#X2 = np.random.normal(mu2, sigma2, N)
#scores = np.concatenate([X1, X2])
#scores = scores[np.logical_and(scores>-1, scores<1)]

#neg = tweets[0]
#pos = tweets[1]
    
#TEST ----------------

fig, ax = plt.subplots(figsize=(12, 6.5))

ax = sns.distplot(scores, bins=20, norm_hist=True, ax=ax, kde=True, hist_kws={'density':True}, kde_kws={'color':'#646464'})
ax.set_xlabel('Sentiment', fontsize=14)
ax.set_ylabel('Density (%)', fontsize=14)
ax.set_xlim(-1, 1)
plt.xticks(fontsize=11)
plt.yticks(fontsize=11)
colors = plt.cm.RdYlGn((np.array([patch.get_x() for patch in ax.patches])+1)/2)

# Assign gradient color
for rec, col in zip(ax.patches, colors):
    rec.set_color(col)

fig.suptitle(f'Sentiment Analysis of @{user}', fontsize=22, x=0.05, y=0.95, ha='left')
fig.tight_layout(rect=[0.1,0.25,0.9,0.9]) 

fig.text(0.175, 0.3, 'Negative', size='12', va='top')
fig.text(0.81, 0.3, 'Positive', size='12', va='top')

fig.text(0.05, 0.21, 'Most positive tweet:', size='12', va='top')
fig.text(0.05, 0.11, 'Most negative tweet:', size='12', va='top')
fig.text(0.23, 0.21, bend(109, pos), wrap=True, font_properties={'style':'italic'}, va='top', size='12', c='#666666')
fig.text(0.23, 0.11, bend(109, neg), wrap=True, font_properties={'style':'italic'}, va='top', size='12', c='#666666')

#plt.show()
plt.savefig('C:\\Users\\pcgou\\eclipse-workspace\\Twitter_Bot\\PieChart.png')
