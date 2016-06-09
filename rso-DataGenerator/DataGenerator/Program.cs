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
using System.Diagnostics;

namespace DataGenerator
{
    class Program
    {

        static void Main(string[] args)
        {
            VoteRepositoryGenerator _voteRepository = new VoteRepositoryGenerator();
            int numOfPackageSent = 0;
            
            string url = ConfigurationManager.ConnectionStrings["URL"].ConnectionString;
            int votesNumberPerPackage = int.Parse(ConfigurationManager.AppSettings["votesNumberPerPackage"]); 
            int numOfPackages = int.Parse(ConfigurationManager.AppSettings["numOfPackages"]);
            int offset = int.Parse(ConfigurationManager.AppSettings["offset"]);

            string dataBase = ConfigurationManager.ConnectionStrings["dataBaseConnectionString"].ConnectionString;

            var json = JsonConvert.SerializeObject(_voteRepository.GetRandomData(votesNumberPerPackage));
            //Console.WriteLine(json);

            Console.WriteLine(string.Format(" **Data generator for RSO_Voting Project 2016** \n ConnectionString: {0}, \n Number of votes per package: {1}.",
                url, votesNumberPerPackage));
            Console.WriteLine(string.Format(" Database ConnectionString: {0} \n",  dataBase));
            Console.WriteLine(" Connecting...\n");
            Stopwatch stopWatch = new Stopwatch();
            stopWatch.Start();

            try
            {
                for (int i = 0; i < numOfPackages; ++i)
                {
                    var request = (HttpWebRequest)WebRequest.Create(url);
                    var postData = json;
                    var data = Encoding.ASCII.GetBytes(postData);

                    request.Method = "POST";
                    request.ContentType = "application/json";
                    request.ContentLength = data.Length;

                    using (var stream = request.GetRequestStream())
                    {
                        stream.Write(data, 0, data.Length);
                    }

                    var response = (HttpWebResponse)request.GetResponse();

                    var responseString = new StreamReader(response.GetResponseStream()).ReadToEnd();
                    numOfPackageSent += votesNumberPerPackage;

                    Console.WriteLine(string.Format(" Connected to {0}. \n\n Package num {1} sent. \n Waiting for {2} sec. \n", url, i + 1, offset));
                    System.Threading.Thread.Sleep(offset*1000);
                }
                Console.WriteLine(string.Format(" All packages sent! \n"));
            }
            catch (Exception ex)
            {
                Console.WriteLine(string.Format(" Unable to connect to {0}. Error: {1}", url, ex));
            }
            stopWatch.Stop();
            
            TimeSpan ts = stopWatch.Elapsed;
            string elapsedTime = String.Format("{0:00}:{1:00}:{2:00}.{3:00}",
                ts.Hours, ts.Minutes, ts.Seconds,
                ts.Milliseconds / 10);

            // Statistics

            Console.WriteLine("\nRunTime " + elapsedTime);
            Console.WriteLine("Number of votes sent: {0}", numOfPackageSent);
            double packPerSec = numOfPackageSent / ts.TotalSeconds;
            Console.WriteLine("Total per second: {0}", packPerSec);

            Console.ReadLine();
        }
    }
}
