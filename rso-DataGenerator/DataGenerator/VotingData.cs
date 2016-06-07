using FizzWare.NBuilder;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Configuration;
using System.Text.RegularExpressions;
using Npgsql;
using System.Data;

namespace DataGenerator
{
    public class VoteEntity
    {
        public string Pesel { get; set; }
        public int Vote { get; set; }
        public int Constituency { get; set; }
        public int Gender { get; set; }
        public int Education { get; set; }
    }

    public class VoteRepository
    {
        private IList<VoteEntity> _votes;

        public IList<VoteEntity> Votes
        {
            get { return _votes; }
            set { _votes = value; }
        }

        public IList<VoteEntity> GetAll()
        {
            return _votes;
        }

        public VoteEntity Get(string pesel)
        {
            return _votes.Single(p => p.Pesel == pesel);
        }

        public IList<VoteEntity> GetByGender(int gender)
        {
            return _votes.Where(p => p.Gender == gender).ToList();
        }

    }

    public class VoteRepositoryGenerator
    {
        private VoteRepository _voteRepository = new VoteRepository();
        public IList<VoteEntity> GetRandomData(int numberOfVotes)
        {
            List<int> vote = new List<int>();
            List<int> constituencies = new List<int>();
            List<int> gender = new List<int>();
            List<int> education = new List<int>();

            vote = GetDictionaryId("candidate_id", "d_candidates");
            constituencies = GetDictionaryId("constituency_id", "d_constituencies");
            gender = GetDictionaryId("sex_id", "d_sex");
            education = GetDictionaryId("education_id", "d_education");

            Random randId = new Random();

            IList<VoteEntity> votes = Builder<VoteEntity>.CreateListOfSize(numberOfVotes)
                .All()
                    .With(p => p.Pesel = GetPesel())
                    .With(v => v.Vote = vote[randId.Next(vote.Count)])
                    .With(c => c.Constituency = constituencies[randId.Next(constituencies.Count)])
                    .With(g => g.Gender = gender[randId.Next(gender.Count)])
                    .With(c => c.Education = education[randId.Next(education.Count)])
                .Build();

            _voteRepository.Votes = votes.ToList();
            IList<VoteEntity> result = _voteRepository.GetAll();
            return result;
        }

        private string GetPesel()
        {
            var numGenerator = new RandomGenerator();
            string month = numGenerator.DateTime().Month.ToString();
            if (month.Length == 1) month = "0" + month;
            string year = numGenerator.Next(20, 98).ToString();
            string day = numGenerator.DateTime().Day.ToString();
            if (day.Length == 1) day = "0" + month;

            return year + month + day + numGenerator.Next(10000, 99999).ToString();
        }

        private List<int> GetDictionaryId(string colName, string tableName)
        {
            List<int> idList = new List<int>();
            string dataBase = ConfigurationManager.ConnectionStrings["dataBaseConnectionString"].ConnectionString;
            NpgsqlConnection conn = new NpgsqlConnection(dataBase);

            conn.Open();
            NpgsqlCommand command = new NpgsqlCommand(string.Format("SELECT {0} FROM {1}", colName, tableName), conn);
            NpgsqlDataReader dr = command.ExecuteReader();
            while (dr.Read())
                idList.Add(int.Parse(dr[0].ToString()));
            conn.Close();

            return idList;
        }

    }
}
