using FizzWare.NBuilder;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Configuration;
using System.Text.RegularExpressions;

namespace DataGenerator
{
    public class VoteEntity
    {
        public string Pesel { get; set; }
        public string Vote { get; set; }
        public string VotingArea { get; set; }
        public Gender Gender { get; set; }
        public string Address { get; set; }
        public string Education { get; set; }
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

        public IList<VoteEntity> GetByGender(Gender gender)
        {
            return _votes.Where(p => p.Gender == gender).ToList();
        }

    }

    public class VoteRepositoryGenerator
    {
        private VoteRepository _voteRepository = new VoteRepository();
        public IList<VoteEntity> GetRandomData(int numberOfVotes)
        {
            IList<VoteEntity> votes = Builder<VoteEntity>.CreateListOfSize(numberOfVotes)
                .All()
                    .With(p => p.Pesel = GetPesel())
                    .With(c => c.Address = Faker.Address.StreetAddress() + ", " + Faker.Address.City())
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
    }
}
