using FizzWare.NBuilder;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using System.Net;
using System.IO;
using System.Configuration;

namespace DataGenerator
{
    class Program
    {

        static void Main(string[] args)
        {
            VoteRepositoryGenerator _voteRepository = new VoteRepositoryGenerator();
            
            string url = ConfigurationManager.ConnectionStrings["URL"].ConnectionString;
            int votesNumberPerPackage = int.Parse(ConfigurationManager.AppSettings["votesNumberPerPackage"]); 
            int packageNumber = 1; //TODO
            int interval = 0; //TODO

            var json = JsonConvert.SerializeObject(_voteRepository.GetRandomData(votesNumberPerPackage));
            //Console.WriteLine(json);

            Console.WriteLine(string.Format(" **Data generator for RSO_Voting Project 2016** \n ConnectionString: {0}, \n Number of votes per package: {1}, \n Number of packages: {2}, Interval: {3}.\n\n",
                url, votesNumberPerPackage, packageNumber, interval));

            try
            {
                var request = (HttpWebRequest)WebRequest.Create(url);
                var postData = json;
                var data = Encoding.ASCII.GetBytes(postData);

                request.Method = "POST";
                request.ContentType = "application/x-www-form-urlencoded";
                request.ContentLength = data.Length;

                using (var stream = request.GetRequestStream())
                {
                    stream.Write(data, 0, data.Length);
                }

                var response = (HttpWebResponse)request.GetResponse();

                var responseString = new StreamReader(response.GetResponseStream()).ReadToEnd();
                Console.WriteLine(string.Format("Connected to {0}. /n All votes sended", url));
            }
            catch (Exception ex)
            {
                Console.WriteLine(string.Format("Unable to connect to {0}. Error: {1}", url, ex));
            }

            Console.ReadLine();
        }
    }
}
