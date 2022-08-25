import pandas as pd
import streamlit as st
from pinotdb import connect
import plotly.express as px

st.set_page_config(layout="wide")
st.title('Real-time Game Analytics')

conn = connect(host='localhost', port=8099, path='/query/sql', scheme='http')
curs = conn.cursor()

st.subheader('Overview')

query = """select
  count(*) as total_events
from events 
limit 1
"""

curs = conn.cursor()
curs.execute(query)
df_summary = pd.DataFrame(curs, columns=[item[0] for item in curs.description])

metric1, metric2, metric3 = st.columns(3)
metric1.metric(
  label="Total Events", 
  value=df_summary['total_events'].values[0]
)

st.subheader('Events Per Minute')

query = """
select
ToDateTime(DATETRUNC('minute', ts), 'yyyy-MM-dd hh:mm:ss') AS dateMin, 
count(*) AS events
from events 
where ts > ago('PT1H')
group by dateMin
order by dateMin desc
LIMIT 30
"""

curs.execute(query)
df_ts = pd.DataFrame(curs, columns=[item[0] for item in curs.description])
df_ts_melt = pd.melt(df_ts, id_vars=['dateMin'], value_vars=['events'])

fig = px.line(df_ts_melt, x='dateMin', y="value", color='variable', color_discrete_sequence =['blue'])
fig['layout'].update(margin=dict(l=0,r=0,b=0,t=40))
fig.update_yaxes(range=[0, df_ts["events"].max() * 1.1])
st.plotly_chart(fig, use_container_width=True)

st.subheader('New User Registrations')

# query = """
# select
# ToDateTime(DATETRUNC('minute', ts), 'yyyy-MM-dd hh:mm:ss') AS dateMin, 
# count(*) AS events
# from events 
# where ts > ago('PT1H') and type='NEW_USER'
# group by dateMin
# order by dateMin desc
# LIMIT 30
# """

# curs.execute(query)
# df_ts = pd.DataFrame(curs, columns=[item[0] for item in curs.description])
# df_ts_melt = pd.melt(df_ts, id_vars=['dateMin'], value_vars=['events'])

# fig = px.line(df_ts_melt, x='dateMin', y="value", color='variable', color_discrete_sequence =['blue'])
# fig['layout'].update(margin=dict(l=0,r=0,b=0,t=40))
# fig.update_yaxes(range=[0, df_ts["events"].max() * 1.1])
# st.plotly_chart(fig, use_container_width=True)