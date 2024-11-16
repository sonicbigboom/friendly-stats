import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ReferenceLine, ResponsiveContainer } from 'recharts';
import { PureComponent, ReactElement, useContext, useEffect, useState } from "react";
import { TokenContext } from '../../data/Token/TokenContext';
import GameRecordExpanded from '../../classes/GameRecordExpanded';
import { MembersContext } from '../../data/Members/MembersContext';

type Props = {
  groupID: number;
  gameTypeID: number;
  forCash: boolean;
  seasonID: number;
  userID: number;
};

export default function ScoreboardPanel( { groupID, gameTypeID, forCash, seasonID, userID }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const [ records, setRecords ] = useState<GameRecordExpanded[]>([]);
  const { getMember } = useContext(MembersContext);


  const forCashParameter = `?forCash=${forCash}`;
  const gameTypeIDParameter = (gameTypeID == -1) ? "" : `&gameTypeID=${gameTypeID}`
  const seasonIDParameter = (seasonID == -1) ? "" : `&seasonID=${seasonID}`
  const userIDParameter = (userID == -1) ? "" : `&userID=${userID}`

  const parameters = `${forCashParameter}${gameTypeIDParameter}${seasonIDParameter}${userIDParameter}`

  useEffect(() => {
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupID}/records${parameters}`,
      {
        method: "GET",
        headers: new Headers({ Authorization: token, "content-type": "application/json" })
      }
    ).then(
      async (response) => {
        if (!response.ok) {
          throw response.status;
        }

        let json = await response.json();

        setRecords(json)
      }
    );
  }, [groupID, gameTypeID, forCash, seasonID, userID])

  let people = new Set<number>()
  let totals: {[id: number] : number} = {}
  let time: {[time: number] : any} = {}
  
  for (const record of records) {
    people.add(record.personID)

    let date = new Date(record.date)
    let t = Math.floor(date.getTime() / (60 * 1000))

    if (!(t in time)) {
      time[t] = { x: t }
    }

    if (!(record.personID in totals)) {
      totals[record.personID] = 0
    }
    totals[record.personID] += record.scoreChange;

    time[t][`person-${record.personID}`] = totals[record.personID]
  }

  let minT = Number.MAX_VALUE
  let maxT = 0
  for (const t in time) {
    let ti = Number(t)
    if (ti < minT) { minT = ti }
    if (ti > maxT) { maxT = ti }
  }
  minT -= 30;
  maxT += 30;

  time[minT] = { x: minT }
  time[maxT] = { x: maxT }

  for (const id of Array.from(people)) {
    time[minT][`person-${id}`] = 0
    time[maxT][`person-${id}`] = totals[id]
  }

  const data = new Array()
  for (const t in time) {
    data.push(time[t])
  }

  const lines: ReactElement[] = []
  for (const id of Array.from(people)) {
    const member = getMember(groupID, id);
    lines.push(
      <Line 
        key={id}
        name={`${member.firstName} ${member.lastName}`} 
        connectNulls 
        type="linear" 
        dataKey={`person-${id}`} 
        stroke={getRandomColor()} 
      />
    )
  }

  return (
    <ResponsiveContainer width="100%" height={800}>
      <LineChart
        width={1500}
        height={800}
        data={data}
        margin={{
          top: 50,
          right: 30,
          left: 20,
          bottom: 100,
        }}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis 
          dataKey="x"
          domain={["auto", "auto"]} 
          scale="time"
          type="number"
          tick={<CustomizedAxisTick/>}
          allowDuplicatedCategory={false}
          allowDataOverflow={true}
        />
        <YAxis />
        <Tooltip />
        <Legend verticalAlign="top" />
        {lines}

        <ReferenceLine y={0} stroke="gray" strokeWidth={1.5} strokeOpacity={0.5}/>
      </LineChart>
    </ResponsiveContainer>
  );
}

function getRandomColor() {
  var letters = '0123456789ABCDEF';
  var color = '#';
  for (var i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}

function getDay(date:Date) {
  const year = date.getFullYear();
  const month = padTo2Digits(date.getMonth() + 1);
  const day = padTo2Digits(date.getDate());

  return [year, month, day].join('/');
}

function padTo2Digits(num: number) {
  return num.toString().padStart(2, '0');
}

class CustomizedAxisTick extends PureComponent {
  render() {
    const { x, y, stroke, payload }:any = this.props;

    return (
      <g transform={`translate(${x},${y})`}>
        <text x={0} y={0} dy={16} textAnchor="end" fill="#666" transform="rotate(-35)">
          {getDay(new Date(payload.value * 60 * 100))}
        </text>
      </g>
    );
  }
}